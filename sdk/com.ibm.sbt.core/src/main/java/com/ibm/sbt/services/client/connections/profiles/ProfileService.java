/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.base.CommonConstants.AT;
import static com.ibm.sbt.services.client.base.CommonConstants.AUTH_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.CH_COLON;
import static com.ibm.sbt.services.client.base.CommonConstants.COMMA;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.DOT;
import static com.ibm.sbt.services.client.base.CommonConstants.EMPTY;
import static com.ibm.sbt.services.client.base.CommonConstants.EQUALS;
import static com.ibm.sbt.services.client.base.CommonConstants.IMAGE_;
import static com.ibm.sbt.services.client.base.CommonConstants.IMAGE_JPG;
import static com.ibm.sbt.services.client.base.CommonConstants.JPG;
import static com.ibm.sbt.services.client.base.CommonConstants.LOCATION_HEADER;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.HREF;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.USERID;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.ACCEPTED;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.COLLEAGUE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTIONID;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION_TYPE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.CONNECTION_UNIQUE_IDENTIFIER;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.EMAIL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FORMAT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FULL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.OUTPUT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.OUTPUT_TYPE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PROFILE;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.PROFILES;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.SOURCEEMAIL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.SOURCEKEY;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.SOURCEUSERID;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.TARGETEMAIL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.TARGETKEY;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.TARGETUSERID;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCARD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.conn.EofSensorInputStream;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;
import com.ibm.sbt.services.client.connections.profiles.serializers.ColleagueConnectionSerializer;
import com.ibm.sbt.services.client.connections.profiles.serializers.ProfileSerializer;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * ProfileService can be used to perform operations related to Profiles. 
 * 
 * @Represents Connections ProfileService
 * @author Swati Singh
 * @author Carlos Manias
 * <pre>
 * Sample Usage
 * {@code
 * 	ProfileService _service = new ProfileService();
 *  Profile profile = _service.getProfile(userId);
 * }
 * </pre>
 */
public class ProfileService extends BaseService {

	private static final long serialVersionUID = -598413531035038479L;

	/**
	 * Constructor Creates ProfileService Object with default endpoint and default cache size
	 */
	public ProfileService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor  - Creates ProfileService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */

	public ProfileService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates ProfileService Object with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */

	public ProfileService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * Constructor  - Creates ProfileService Object with a specified endpoint
	 * 
	 * @param endpoint
	 *            Creates ProfileService with specified endpoint and a default CacheSize
	 */

	public ProfileService(Endpoint endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor - Creates ProfileService Object with specified endpoint and CacheSize
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileService with specified endpoint and CacheSize
	 */
	public ProfileService(Endpoint endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart(AUTH_TYPE, auth);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return PROFILES;
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Profile> getProfileFeedHandler() {
		return new AtomFeedHandler<Profile>(this) {
			@Override
			protected Profile entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Profile(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public ProfileList createEntityList(Response requestData) {
				return new ProfileList((Response)requestData, this);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<ColleagueConnection> getColleagueFeedHandler() {
		return new AtomFeedHandler<ColleagueConnection>(this, false) {
			@Override
			protected ColleagueConnection entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ColleagueConnection(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public ColleagueConnectionList createEntityList(Response requestData) {
				return new ColleagueConnectionList((Response)requestData, this);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Tag> getTagFeedHandler() {
		return new AtomFeedHandler<Tag>(this) {
			@Override
			protected Tag entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Tag(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public TagList createEntityList(Response requestData) {
				return new TagList((Response)requestData, this);
			}
		};
	}
	
	/**
	 * Wrapper method to get profile of a user
	 *
	 * @param id
	 *             unique identifier of User , it can either be email or id 				
	 * @return Profile
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public Profile getProfile(String id) throws ProfileServiceException {
		return getProfile(id, null);
	}

	/**
	 * Wrapper method to get profile of a user
	 *
	 * @param id
	 *             unique identifier of User , it can either be email or id 	
	 * @param parameters          			
	 * @return Profile
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public Profile getProfile(String id, Map<String, String> parameters) throws ProfileServiceException {
		// TODO: Do a cache lookup first. If cache miss, make a network call to get profile

		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		parameters.put(getIdParameter(id), id);
		String url = ProfileUrls.PROFILE.format(this);
		
		return getProfileEntity(url, parameters);
	}

	/**
	 * Wrapper method to search profiles based on different parameters
	 * 
	 * @param parameters 
	 * 				  list of query string parameters to pass to API
	 * @return list of searched Profiles
	 * @throws ClientServicesException 
	 */
	public ProfileList searchProfiles( Map<String, String> parameters) throws ProfileServiceException {
		if (null == parameters) {
			parameters = new HashMap<String, String>();
		}
		String url = ProfileUrls.SEARCH.format(this);
		
		return getProfileEntityList(url, parameters);
	}

	/** Wrapper method to get user's tags
	 * 
	 * @param id 
	 *        unique identifier of the user whose tags are required, it can be email or userKey
	 * @return TagList
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public TagList getTags(String id) throws ProfileServiceException {
		return getTags(id,null);
	}

	/**
	 * Wrapper method to get user's tags
	 * 
	 * @param id 
	 *        unique identifier of the user whose tags are required, it can be email or userKey
	 * @param parameters 
	 *         list of query string parameters to pass to API
	 * @return TagList 
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public TagList getTags(String id, Map<String, String> parameters) throws ProfileServiceException {
	
	  if (StringUtil.isEmpty(id)){
	    throw new ProfileServiceException(null, Messages.InvalidArgument_1);
	  }
	
	  if(parameters == null)
	    parameters = new HashMap<String, String>();
		String url = ProfileUrls.TAGS.format(this);
	    if(id.contains(AT))
	      parameters.put(TARGETEMAIL, id);
	    else
	      parameters.put(TARGETKEY, id);

		return getTagEntityList(url, parameters);
	  }
	  
	  /**
	   * 
	   * @param id
	   * @param tags Comma-separated list of tags to add to the profile
	   * @throws ProfileServiceException 
	   */
	public void addTags(String sourceId, String targetId, Profile profile) throws ProfileServiceException{
		if (StringUtil.isEmpty(sourceId)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}

		if (StringUtil.isEmpty(targetId)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		HashMap<String, String> parameters = new HashMap<String, String>();

		if(sourceId.contains(AT))
			parameters.put(SOURCEEMAIL, sourceId);
		else
	      parameters.put(SOURCEKEY, sourceId);

	    if(targetId.contains(AT))
	      parameters.put(TARGETEMAIL, targetId);
	    else
	      parameters.put(TARGETKEY, targetId);
	    
	    String serviceUrl = ProfileUrls.TAGS.format(this);
	    
	    ProfileSerializer serializer = new ProfileSerializer(profile);
	    try {
			Response response = updateData(serviceUrl, parameters, serializer.tagsPayload(), null);
			checkResponseCode(response, HTTPCode.OK);
		} catch (ClientServicesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  
	/**
	 * Wrapper method to get user's colleagues
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @return Profiles of User's colleagues
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ProfileList getColleagues(String id) throws ProfileServiceException {
		return getColleagues(id,null);
	}

	/**
	 * Wrapper method to get user's colleagues
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Profiles of User's colleagues  
	 * @throws ClientServicesException 
	 * @throws ProfileServiceException
	 */
	public ProfileList getColleagues(String id, Map<String, String> parameters) throws ProfileServiceException {
		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = ProfileUrls.CONNECTIONS.format(this);
		parameters.put(getIdParameter(id), id);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, PROFILE);

		return getProfileEntityList(url, parameters);
	}

	/**
	 * Wrapper method to get colleague connections
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @return list of colleague connections 
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnectionList getColleagueConnections(String id) throws ProfileServiceException {
		return getColleagueConnections(id,null);
	}

	/**
	 * Wrapper method to get colleague connections
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose colleagues are required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return list of colleague connections   
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnectionList getColleagueConnections(String id, Map<String, String> parameters) throws ProfileServiceException {
		if (StringUtil.isEmpty(id)){
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		if(parameters == null)
			parameters = new HashMap<String, String>();
		String url = ProfileUrls.CONNECTIONS.format(this);
		parameters.put(getIdParameter(id), id);
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, CONNECTION);

		return getColleagueConnectionEntityList(url, parameters);
	}

	/**
	 * Wrapper method to get check if two users are colleagues
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return ColleagueConnection
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnection checkColleague(String sourceId, String targetId) throws ProfileServiceException {
		return checkColleague(sourceId, targetId, null);
	}
	/**
	 * Wrapper method to get check if two users are colleagues
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return ColleagueConnection
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnection checkColleague(String sourceId, String targetId, Map<String, String> parameters) throws ProfileServiceException {

		if (StringUtil.isEmpty(sourceId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = ProfileUrls.CONNECTION.format(this);
		if (isEmail(sourceId)) {
			parameters.put(SOURCEEMAIL, sourceId);
		} else {
			parameters.put(SOURCEUSERID, sourceId);
		}
		if (isEmail(targetId)) {
			parameters.put(TARGETEMAIL, targetId);
		} else {
			parameters.put(TARGETUSERID, targetId);
		}
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		
		return getColleagueConnectionEntity(url, parameters);
	}

	/**
	 * Wrapper method to get profiles of colleagues shared by two users
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleagues profiles
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ProfileList getCommonColleagues(String sourceId, String targetId) throws ProfileServiceException {
		return getCommonColleagues(sourceId, targetId, null);
	}

	/**
	 * Wrapper method to get profiles of colleagues shared by two users
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleagues profiles
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ProfileList getCommonColleagues(String sourceId, String targetId,  Map<String, String> parameters) throws ProfileServiceException {

		if (StringUtil.isEmpty(sourceId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = ProfileUrls.CONNECTIONS_IN_COMMON.format(this);
		if (isEmail(sourceId)) {
			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(COMMA).append(targetId);
			parameters.put(EMAIL, value.toString());
		} else {

			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(COMMA).append(targetId);
			parameters.put(USERID, value.toString());
		}
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, PROFILE);

		return getProfileEntityList(url, parameters);
	}

	/**
	 * Wrapper method to get colleague connections
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleague connections
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnectionList getCommonColleagueConnections(String sourceId, String targetId) throws ProfileServiceException {
		return getCommonColleagueConnections(sourceId, targetId, null);
	}

	/**
	 * Wrapper method to get colleague connections
	 * 
	 * @param sourceId 
	 * 				 userid or email of first user
	 * @param targetId 
	 * 				 userid or email of second user
	 * @return list of common colleague connections
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ColleagueConnectionList getCommonColleagueConnections(String sourceId, String targetId,  Map<String, String> parameters) throws ProfileServiceException {

		if (StringUtil.isEmpty(sourceId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_4);
		}
		if (StringUtil.isEmpty(targetId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_5);
		}
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		String url = ProfileUrls.CONNECTIONS_IN_COMMON.format(this);
		if (isEmail(sourceId)) {
			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(",").append(targetId);
			parameters.put(EMAIL, value.toString());
		} else {

			StringBuilder value =  new StringBuilder(sourceId);
			value = value.append(COMMA).append(targetId);
			parameters.put(USERID, value.toString());
		}
		parameters.put(CONNECTION_TYPE, COLLEAGUE);
		parameters.put(OUTPUT_TYPE, CONNECTION);

		return getColleagueConnectionEntityList(url, parameters);
	}

	/**
	 * Wrapper method to get a user's report to chain
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @return List of Profiles 
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ProfileList getReportingChain(String id) throws ProfileServiceException {
		return getReportingChain(id,null);
	}

	/**
	 * Wrapper method to get a user's report to chain
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose report to chain is required, it can be email or userID
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return List of Profiles
	 * @throws ProfileServiceException	
	 * @throws ClientServicesException 
	 */
	public ProfileList getReportingChain (String id, Map<String, String> parameters)throws ProfileServiceException {

		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();
		parameters.put(getIdParameter(id), id);
		String url = ProfileUrls.REPORTING_CHAIN.format(this);
		return getProfileEntityList(url, parameters);
	}

	/**
	 * Wrapper method to get a person's people managed
	 * 
	 * @param id 
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @return List of Profiles 
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
	public ProfileList getPeopleManaged(String id) throws ProfileServiceException {
		return getPeopleManaged(id,null);
	}

	/**
	 * Wrapper method to get a person's people managed
	 * 
	 * @param id
	 * 		   unique identifier of the user whose direct reports are required, it can be email or userID
	 * @param parameters
	 * 		   list of query string parameters to pass to API
	 * @return List of Profiles
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 * 
	 */
	public ProfileList getPeopleManaged(String id, Map<String, String> parameters)throws ProfileServiceException {

		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}
		if(parameters == null)
			parameters = new HashMap<String, String>();
		parameters.put(getIdParameter(id), id);
		String url = ProfileUrls.PEOPLE_MANAGED.format(this);

		return getProfileEntityList(url, parameters);
	}

	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * <p>
	 * a default Invite message is used while sending the invite
	 *  
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @return Id of the Connection
	 * @throws ProfileServiceException
	 */
	public String sendInvite(String id)throws ProfileServiceException{
		String defaultInviteMsg = Messages.SendInviteMsg;
		return sendInvite(id, defaultInviteMsg);
	}

	/**
	 * Wrapper method to send Invite to a user to become colleague
	 * 
	 * @param id
	 * 		   unique identifier of the user to whom the invite is to be sent, it can be email or userID
	 * @param inviteMsg 
	 * 				Invite message to the other user
	 * @return Id of the Connection
	 * @throws ProfileServiceException
	 */
	public String sendInvite(String id, String inviteMsg)throws ProfileServiceException {
		if (StringUtil.isEmpty(id)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_1);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			String url = ProfileUrls.CONNECTIONS.format(this);
			parameters.put(getIdParameter(id), id);
			parameters.put(CONNECTION_TYPE, COLLEAGUE);
			Object payload = constructSendInviteRequestBody(inviteMsg);
			Response response = super.createData(url, parameters, payload);
			return extractConnectionIdFromHeaders(response);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.SendInviteException, id);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.SendInviteException, id);
		}

	}

	/**
	 * Wrapper method to accept a Invite 
	 * 
	 * @param ColleagueConnection 
	 * @throws ProfileServiceException
	 * 
	 */
	public void acceptInvite(ColleagueConnection connection)throws ProfileServiceException{

		if (connection == null) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_6);
		}
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(CONNECTIONID, connection.getConnectionId());
			String url = ProfileUrls.CONNECTION.format(this);
			Object payload = constructAcceptInviteRequestBody(connection, ACCEPTED);

			super.updateData(url, parameters,payload, connection.getConnectionId());
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.AcceptInviteException, connection.getConnectionId());
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.AcceptInviteException, connection.getConnectionId());
		}
	}

	/**
	 * Wrapper method is used to delete/ignore a Invite 
	 * 
	 * @param connectionId 
	 * 					unique id of the connection
	 * @throws ProfileServiceException
	 */
	public void deleteInvite(String connectionId)throws ProfileServiceException{

		if (StringUtil.isEmpty(connectionId)) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_2);
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(CONNECTIONID, connectionId);
			String url = ProfileUrls.CONNECTION.format(this);
			super.deleteData(url, parameters, connectionId);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.DeleteInviteException, connectionId);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.DeleteInviteException, connectionId);
		}
	}

	/**
	 * Wrapper method to update a User's profile
	 * 
	 * @param Profile
	 * @throws ProfileServiceException
	 */
	public void updateProfile(Profile profile) throws ProfileServiceException{
		if (profile == null) {
			throw new ProfileServiceException(null, Messages.InvalidArgument_3);
		}
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(OUTPUT, VCARD);
			parameters.put(FORMAT, FULL);
			String id = profile.getUserid();
			parameters.put(getIdParameter(id), id);
			Object updateProfilePayload = constructUpdateRequestBody(profile);
			String updateUrl = ProfileUrls.PROFILE_ENTRY.format(this);
			super.updateData(updateUrl, parameters,updateProfilePayload, getIdParameter(profile.getAsString("uid")));
			profile.clearFieldsMap();
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfileException);
		} catch (IOException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfileException);
		}

	}
	
	/**
	 * Returns the userid of the currently connected user
	 * 
	 * @return
	 * @throws ProfileServiceException
	 */
	public String getMyUserId()throws ProfileServiceException{
		String id = "";
		String peopleApiUrl = ProfileUrls.MY_USER_ID.format(this);
		try {
			Response feed = getClientService().get(peopleApiUrl);
			JsonDataHandler dataHandler = new JsonDataHandler((JsonJavaObject)feed.getData());
			id = dataHandler.getAsString("entry/id");
			id = id.substring(id.lastIndexOf(CH_COLON)+1, id.length());
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.ProfileException, id);
		}
		return id;
		
	}

	/**
	 * Returns the profile of the currently connected user
	 * 
	 * @return
	 * @throws ProfileServiceException
	 * @throws ClientServicesException
	 */
	public Profile getMyProfile() throws ProfileServiceException {
		return getProfile(getMyUserId(), null);
	}
	
	/**
	 * Wrapper method to update a User's profile photo
	 * 
	 * @param File
	 * 			image to be uploaded as profile photo
	 * @param userid
	 * @throws ProfileServiceException
	 */
	public void updateProfilePhoto(File file, String userId) throws ProfileServiceException {
        if (file == null || !file.canRead()) {
            throw new ProfileServiceException(null, Messages.MessageCannotReadFile,
                    file.getAbsolutePath());
        }
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(getIdParameter(userId), userId);
			String name = file.getName();
			int dot = StringUtil.lastIndexOfIgnoreCase(name, DOT);
			String ext = "";
			if (dot > -1) {
				ext = name.substring(dot + 1); // add one for the dot!
			}
			if (!StringUtil.isEmpty(ext)) {
				Map<String, String> headers = new HashMap<String, String>();
				if (StringUtil.equalsIgnoreCase(ext, JPG)) {
					headers.put(CONTENT_TYPE, IMAGE_JPG);	// content-type should be image/jpeg for file extension - jpeg/jpg
				} else {
					headers.put(CONTENT_TYPE, IMAGE_ + ext);
				}
				String url = ProfileUrls.PHOTO.format(this);
				getClientService().put(url, parameters, headers, file, ClientService.FORMAT_NULL);
				
			}
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e, Messages.UpdateProfilePhotoException);
		}
	
	}

	/**
	 * Returns a mapping containing the extended attributes for the entry.<bt/>
	 * This method execute a xhr call to the back end for every attribute.
	 * 
	 * @param p the profile to use. has to be a full profile, to obtain all the extended attributes links
	 * @return a map containing the id of the attribute as key and the attribute value as value<br/>
	 * the map can contain object of type InputStream, Node, String, according to the return content type
	 * @throws ProfileServiceException
	 */
	public Map<String,Object> getExtendedAttributes(Profile p) throws ProfileServiceException {
		Map<String, Object> ret = new HashMap<String, Object>();
 		List<Node> attributes = (List<Node>) p.getDataHandler().getEntries(ProfileXPath.extendedAttributes);
		for (Node link :attributes) {
	         String extUrl = link.getAttributes().getNamedItem(HREF).getTextContent();
	         String extId = link.getAttributes().getNamedItem("snx:extensionId").getTextContent();
	         Response resp;
			try {
				resp = getEndpoint().xhrGet(extUrl);
			} catch (ClientServicesException e) {
				throw new ProfileServiceException(e);
			}

			Object extValue= resp.getData();
			if (resp.getData() instanceof EofSensorInputStream) {
				//a EofSensorInputStream leaves the connection open until it is read fully
				//so we read it before serving it to the client terminating the connection
				try {
					extValue = new ByteArrayInputStream(IOUtils.toByteArray((EofSensorInputStream)resp.getData()));
				} catch (Exception e) {
					throw new ProfileServiceException(e,"Exception reading " + extUrl);
				}
			}
			ret.put(extId, extValue);
		}
		return ret;
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 */
	protected Object constructCreateRequestBody(Profile profile) {
		ProfileSerializer serializer = new ProfileSerializer(profile);
		return serializer.createPayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Update operations
	 * @return Object
	 */
	protected Object constructUpdateRequestBody(Profile profile) {
		ProfileSerializer serializer = new ProfileSerializer(profile);
		return serializer.updatePayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 */
	protected Object constructAcceptInviteRequestBody(ColleagueConnection connectionEntry, String action) {
		ColleagueConnectionSerializer serializer = new ColleagueConnectionSerializer(connectionEntry);
		return serializer.acceptInvitePayload();
	}

	/*
	 * This method is used by ProfileService wrapper methods to construct request body for Add operations
	 * @return Object
	 * @throws ProfileServiceException 
	 */
	protected Object constructSendInviteRequestBody(String inviteMsg) throws ProfileServiceException {
		ColleagueConnection colleagueConnection = new ColleagueConnection(this, null);
		colleagueConnection.setContent(inviteMsg);
		ColleagueConnectionSerializer serializer = new ColleagueConnectionSerializer(colleagueConnection);
		return serializer.acceptInvitePayload();
	}

	protected String getIdParameter(String id){
		return isEmail(id)?EMAIL:USERID;
	}

	protected String extractConnectionIdFromHeaders(Response requestData){
		Header header = requestData.getResponse().getFirstHeader(LOCATION_HEADER);
		String urlLocation = header!=null?header.getValue():EMPTY;
		return urlLocation.substring(urlLocation.indexOf(CONNECTION_UNIQUE_IDENTIFIER+EQUALS) + (CONNECTION_UNIQUE_IDENTIFIER+EQUALS).length());
	}
	
	protected Profile getProfileEntity(String requestUrl, Map<String, String> parameters) throws ProfileServiceException {
		try {
			return (Profile)getEntity(requestUrl, getParameters(parameters), getProfileFeedHandler());
		} catch (IOException e) {
			throw new ProfileServiceException(e);
		} catch(ClientServicesException e){
			throw new ProfileServiceException(e);
		}
	}

	protected ColleagueConnection getColleagueConnectionEntity(String requestUrl, Map<String, String> parameters) throws ProfileServiceException {
		try {
			return (ColleagueConnection)getEntity(requestUrl, getParameters(parameters), getColleagueFeedHandler());
		} catch (IOException e) {
			throw new ProfileServiceException(e);
		} catch(ClientServicesException e){
			throw new ProfileServiceException(e);
		}
	}

	protected ProfileList getProfileEntityList(String requestUrl, Map<String, String> parameters) throws ProfileServiceException {
		try {
			return (ProfileList)getEntities(requestUrl, getParameters(parameters), getProfileFeedHandler());
		} catch (IOException e) {
			throw new ProfileServiceException(e);
		} catch(ClientServicesException e){
			throw new ProfileServiceException(e);
		}
	}

	protected ColleagueConnectionList getColleagueConnectionEntityList(String requestUrl, Map<String, String> parameters) throws ProfileServiceException {
		try {
			return (ColleagueConnectionList)getEntities(requestUrl, getParameters(parameters), getColleagueFeedHandler());
		} catch (IOException e) {
			throw new ProfileServiceException(e);
		} catch(ClientServicesException e){
			throw new ProfileServiceException(e);
		}
	}

	protected TagList getTagEntityList(String requestUrl, Map<String, String> parameters) throws ProfileServiceException {
		try {
			return (TagList)getEntities(requestUrl, getParameters(parameters), getTagFeedHandler());
		} catch (IOException e) {
			throw new ProfileServiceException(e);
		} catch(ClientServicesException e){
			throw new ProfileServiceException(e);
		}
	}

}