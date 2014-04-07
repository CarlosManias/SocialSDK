***REMOVED***
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

use Guzzle\Http\Client;

if (file_exists(BASE_PATH . DIRECTORY_SEPARATOR . "core".DIRECTORY_SEPARATOR."system".DIRECTORY_SEPARATOR."libs".DIRECTORY_SEPARATOR."vendor".DIRECTORY_SEPARATOR."autoload.php")) {
	require_once BASE_PATH . "".DIRECTORY_SEPARATOR."core".DIRECTORY_SEPARATOR."system".DIRECTORY_SEPARATOR."libs".DIRECTORY_SEPARATOR."vendor".DIRECTORY_SEPARATOR."autoload.php";
} else {
	require_once BASE_PATH . "".DIRECTORY_SEPARATOR."system".DIRECTORY_SEPARATOR."libs".DIRECTORY_SEPARATOR."vendor".DIRECTORY_SEPARATOR."autoload.php";
}

/**
 * OAuth 1.0 Endpoint
 * 
 * @author Benjamin Jakobus
 */
defined('SBT_SDK') OR exit('Access denied.');
class SBTOAuth1Endpoint extends BaseController implements SBTEndpoint
{
	
	/**
	 * Performs a request to the given request URL.
	 * 
	 * @param string $requestURL		Resource that is to be requested (e.g. https://apps.na.collabserv.com/communities/service/html/mycommunities)
	 * @param string $callbackURL		The callback URL (e.g. http://127.0.0.1:8443/demo/application/OAuthSample.php)
	 * @param string $method			GET, PUT or POST. POST by default
	 */
	public function request($requestURL, $callbackURL, $method = 'POST', $endpointName = 'connections')
	{
		$callbackURL = $callbackURL . "&requestMethod=" . $method . "&requestURL=" . urlencode($requestURL) . "&endpointName=" . $endpointName;
		$store = SBTCredentialStore::getInstance();

		try
		{	
			//  STEP 1:  If we do not have an OAuth token yet, go get one
			if (empty($_GET["oauth_token"])) {							
				$store = SBTCredentialStore::getInstance();
				$settings = new SBTSettings();

				$random = mt_rand(0, 999999);
				$nonce = sha1($random);

				$parameters = array(
					'oauth_version' => '1.0',
					'oauth_callback' => $callbackURL,
					'oauth_timestamp'  => time(),
					'oauth_signature' => $settings->getConsumerSecret($endpointName) . '&' . $settings->getConsumerKey($endpointName),
					'oauth_signature_method' => 'PLAINTEXT',
					'oauth_nonce' => $nonce,
					'oauth_consumer_key' => $settings->getConsumerKey($endpointName)
				);
			
				$tokenURL = $settings->getRequestTokenURL($endpointName) . '?' . http_build_query($parameters, null, '&');
				
				$client = new Client($tokenURL);
				$client->setDefaultOption('verify', false);
				
				$headers = null;
				$body = null;
				$options = array();
				$response = null;
				
				try {
					$request = $client->createRequest($method, $tokenURL, $headers, $body, $options);
					if ($settings->forceSSLTrust($endpointName)) {
						$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
						$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
					}
					$response = $request->send();
				} catch(Guzzle\Http\Exception\BadResponseException $e) {
					$response = $e->getResponse();
					print_r($response->getBody(TRUE));
				}
			
				foreach ($response->getHeaderLines() as $h) {
					if (strpos($h, "Content-Type") === 0) header($h, TRUE);
				}
				
				header(':', true, $response->getStatusCode());
				header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
				
				parse_str($response->getBody(TRUE), $info);
				
				if (isset($info['oauth_token'])) {
					$store->storeRequestToken($info['oauth_token'], $endpointName);
				}
				
				if (isset($info['oauth_token_secret'])) {
					$store->storeRequestTokenSecret($info['oauth_token_secret'], $endpointName);
				}
					
				if (!headers_sent()) {
					header("Location: " . $settings->getAuthorizationURL($endpointName) . "?oauth_token=" . $info['oauth_token']);
				} else {
					echo '<script type="text/javascript" language="javascript">window.location = "' . $settings->getAuthorizationURL($endpointName) . "?oauth_token=" . $info['oauth_token'] . '";</script>';
				}
			}
		}
		catch(OAuth1Exception2 $e) {
			echo "OAuth1Exception2:  " . $e->getMessage();
		}
	}
	
	/**
	 * The callback function for authenticating the user and then storing the token in the CredentialStore (no content
	 * is being requested).
	 *
	 * @author Benjamin Jakobus
	 */
	public function authenticationCallback()
	{
		if (empty($_GET['oauth_token'])) {
			return;
		}
		
		$endpointName = "connections";
		if (isset($_GET['endpointName'])) {
			$endpointName = $_GET['endpointName'];
		}

		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();
		$store->storeRequestToken($_GET['oauth_token'], $endpointName);
		$store->storeVerifierToken($_GET['oauth_verifier'], $endpointName);
		$requestURL = urldecode($_GET['requestURL']);

		
		$this->_getAccessToken($endpointName);

		header("Location: " . $requestURL);
	}
	
	/**
	 * Gets the access token.
	 */
	private function _getAccessToken($endpointName = "connections") 
	{
		$settings = new SBTSettings();
		$store = SBTCredentialStore::getInstance();
		
		$random = mt_rand(0, 999999);
		$nonce = sha1($random);
		
		$parameters = array(
			'oauth_nonce' => $nonce,
			'oauth_version' => '1.0',
			'oauth_token' => $store->getRequestToken($endpointName),
			'oauth_timestamp'  => time(),
			'oauth_signature' => $settings->getConsumerSecret($endpointName) . '&' . $store->getRequestTokenSecret($endpointName),
			'oauth_signature_method' => 'PLAINTEXT',
			'oauth_verifier' => $store->getVerifierToken($endpointName),
			'oauth_consumer_key' => $settings->getConsumerKey($endpointName)
		);
		
		$serviceURL = $settings->getAccessTokenURL($endpointName) . '?' . http_build_query($parameters, null, '&');
		
		$client = new Client($serviceURL);
		
		$client->setDefaultOption('verify', false);
		
		$headers = null;
		$body = null;
		$options = array();
		$response = null;
		
		try {
			$request = $client->createRequest('GET', $serviceURL, $headers, $body, $options);
			if ($settings->forceSSLTrust($endpointName)) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			$response = $request->send();
		} catch(Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
			$store->deleteOAuthCredentials($endpointName);
			print_r($response->getBody(TRUE));
			header('X-PHP-Response-Code: ' . $response->getStatusCode(), true, $response->getStatusCode());
			die("Your tokens expired. Make sure you are logged out of SmartCloud, clear your cache and cookies and try again.");
		}
		
		parse_str($response->getBody(TRUE), $info);
		
		if (isset($info['oauth_token'])) {
			$store->storeOAuthAccessToken($info['oauth_token'], $endpointName);
		}
		
		if (isset($info['oauth_token_secret'])) {
			$store->storeTokenSecret($info["oauth_token_secret"], $endpointName);
		}
		
	}
	
	/**
	 * Makes the request to the server.
	 * 
	 * @param string $server	
	 * @param string $service		The rest service to access e.g. /connections/communities/all
	 * @param string $method		GET, POST or PUT
	 * @param string $body
	 * @param string $headers
	 */
	public function makeRequest($server, $service, $method, $options, $body = null, $headers = null, $endpointName = "connections") 
	{
		$store = SBTCredentialStore::getInstance();
		$settings = new SBTSettings();
		
		$random = mt_rand(0, 999999);
		$nonce = sha1($random);
		
		if ($store->getOAuthAccessToken($endpointName) == null) {
			$this->_getAccessToken($endpointName);
		}

		$url = $server . '/' . $service;

		$client = new Client($url);
		$client->setDefaultOption('verify', false);

		$options = array();
		$response = null;
		try {			
			$request = $client->createRequest($method, $url, $headers, $body, $options);
			$request->addHeader('Authorization', 'OAuth oauth_nonce="' . $nonce . '",oauth_version="1.0", oauth_timestamp="' . time() . '",oauth_signature="' . $settings->getConsumerSecret($endpointName) . '&' .$store->getTokenSecret($endpointName) 
					. '",oauth_signature_method="PLAINTEXT",oauth_consumer_key="'.$settings->getConsumerKey($endpointName).'",oauth_token="' . $store->getOAuthAccessToken($endpointName) . '"');
			if ($settings->forceSSLTrust($endpointName)) {
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYHOST, false);
				$request->getCurlOptions()->set(CURLOPT_SSL_VERIFYPEER, false);
			}
			if ($method == 'POST' && isset($_FILES['file']['tmp_name'])) {
				$request->addPostFile('file', $_FILES['file']['tmp_name']);
			}
			$response = $request->send();
		} catch(Guzzle\Http\Exception\BadResponseException $e) {
			$response = $e->getResponse();
		}
		
		return $response;
	}
}
