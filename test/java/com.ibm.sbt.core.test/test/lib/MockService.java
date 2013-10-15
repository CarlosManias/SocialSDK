package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.conn.EofSensorInputStream;
import org.junit.Test;
import org.w3c.dom.Node;

import com.ibm.commons.util.io.base64.Base64InputStream;
import com.ibm.commons.util.io.base64.Base64OutputStream;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.Endpoint;

public class MockService extends ClientService {
	
	private enum MockMode { RECORD, REPLAY, PASSTHRU }
	//private MockMode mode = MockMode.PASSTHRU;
	private MockMode mode = MockMode.REPLAY;
	private ClientService service;

	//used to know when to append and when to reset the mock file; //TODO will not work if we test with multiple endpoints in the same test run
	private static final HashSet<String> seen = new HashSet<String>();
	private static final HashMap<String, BufferedReader> replyStream = new HashMap<String, BufferedReader>();

    public MockService(ClientService svc) {
    	this.service = svc;
    }

    public MockService(Endpoint endpoint) {
        super(endpoint);
    }

    public MockService(String endpointName) {
        super(endpointName);
    }
    
    @Override
	public Response xhr(String method, Args args, Object content) throws ClientServicesException {
    	Response response = null;
    	switch(this.mode){
	    	case REPLAY:
	    		return replayResponse();
	    		
	    	case PASSTHRU:
	    		return service.xhr(method, args, content);
	    	case RECORD:
	    		try {
	    		response = service.xhr(method, args, content);
	    		} catch (ClientServicesException e) {
	    			recordResponse(e);
	    			throw e;
	    		}
	    		catch (RuntimeException e) {
	    			recordResponse(e);
	    			throw e;
	    		} 
	    		recordResponse(response);
	    		break;
    	}
    	return response;
	}

	private void recordResponse(Response response) {
		try {
			File file = getFile(true);
			FileWriter fstream;

			fstream = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fstream);
			if (Node.class.isAssignableFrom(response.getData().getClass()))
				out.write("xml:");
			else if (EofSensorInputStream.class.isAssignableFrom(response.getData().getClass()))
				out.write("eofstream:");
			else	
			out.write("json:");
				
			out.write(String.valueOf( response.getResponse().getStatusLine().getStatusCode() ).trim());
			out.write(":");
			out.write(serialize(response.getResponseHeaders()));
			out.write(":");
			out.write(serialize(response.getData()));
			out.write(":\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void recordResponse(Throwable response) {
		try {
			File file = getFile(true);
			FileWriter fstream;

			fstream = new FileWriter(file, true);
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write("throwable:");
	        out.write(serialize(response));
	        out.write(":\n");
	        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    String serialize(EofSensorInputStream is) throws IOException {    	

	
    	ByteArrayOutputStream w = new ByteArrayOutputStream();
    	
    	Base64OutputStream base64OutputStream = new Base64OutputStream(w);
		IOUtils.copy(is,base64OutputStream);
		base64OutputStream.flush();base64OutputStream.close();
    	System.out.println("CONVERTED OBJECT TO "+w.toString("UTF-8"));
    	return w.toString("UTF-8");
    }

    
    
    String serialize(Object o) throws IOException {    	
    	if (o instanceof EofSensorInputStream) return serialize((EofSensorInputStream) o);
    	ByteArrayOutputStream w = new ByteArrayOutputStream();
    	Base64OutputStream base64OutputStream = new Base64OutputStream(w);
		ObjectOutputStream os = new ObjectOutputStream(base64OutputStream);
    	os.writeObject(o);
    	os.flush();os.close();
    	base64OutputStream.flush();base64OutputStream.close();
    	System.out.println("CONVERTED OBJECT TO "+w.toString("UTF-8"));
    	return w.toString("UTF-8");
    }

    
    Object  deserialize(String o) throws IOException {
    	ObjectInputStream is = new ObjectInputStream(new Base64InputStream(new ByteArrayInputStream(o.getBytes())));
    	try {
			return is.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
    }

	private Response replayResponse() throws ClientServicesException {
		
		Response response = null;
		
		try {
			BufferedReader r = getReader();
			String line = r.readLine();
			String[] parts = line.split(":");
			if (parts[0].equals("throwable")) {
				Object o = deserialize(parts[1]);
				if (o instanceof ClientServicesException) {
					throw (ClientServicesException) o;
				}
				if (o instanceof RuntimeException) {
					throw (RuntimeException) o;
				}
				throw new ClientServicesException((Throwable) o);
			}
			
			if (parts[0].equals("eofstream")) {		
				EofSensorInputStream st = null;
				if (parts.length==4)
				st = new EofSensorInputStream(
						new Base64InputStream(new ByteArrayInputStream(parts[3].getBytes())),null);
				else
					st = new EofSensorInputStream(new ByteArrayInputStream("".getBytes()),null);
				Response ret = new Response(st);
				ret.setHeaders((Header[])deserialize(parts[2]));
				
				//TODO: set status code
				return ret;
			}

			Response ret = new Response(deserialize(parts[3]));
			ret.setHeaders((Header[])deserialize(parts[2]));
			//TODO: set status code
			return ret;
			
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}

	private StackTraceElement getStackTraceElement() {
		StackTraceElement last = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		for (StackTraceElement trace : stackTraceElements) {
			if (trace.getClassName().endsWith("Test")) {

				try {
					if (Class.forName(trace.getClassName())
							.getMethod(trace.getMethodName())
							.isAnnotationPresent(Test.class))
						last = trace;
				} catch (Exception e) {
				}
			}
		}
		return last;
	}

	private File getFile(boolean write) throws IOException {
		String path = getPath();
		boolean reset = !seen.contains(path);
		seen.add(path);
		
		File file = new File(path);
		File parentFolder = new File(file.getParent());
		parentFolder.mkdirs();
		if (write && reset && file.exists())
			file.delete();
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	private BufferedReader getReader() throws IOException {
		String path = getPath();
		if (replyStream.containsKey(path)){
			return replyStream.get(path);
		}
		BufferedReader ret = new BufferedReader(new FileReader(new File(path)));
		replyStream.put(path,ret);
		return ret;
	}
	
	private String getPath() {
		StackTraceElement trace = getStackTraceElement();
		String basePath = System.getProperty("user.dir");
		String className = trace.getClassName().replace(".", File.separator);
		String methodName = trace.getMethodName();
		String path = new StringBuilder(basePath).append(File.separator)
				.append("test").append(File.separator).append(className)
				.append(File.separator).append(methodName).append(".mock")
				.toString();
		return path;
	}

}
