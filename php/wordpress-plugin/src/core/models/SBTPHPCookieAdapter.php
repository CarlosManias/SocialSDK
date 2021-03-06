<?php
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

/**
 * Encapsulated $_COOKIE.
 * 
 * @author Benjamin Jakobus
 */
class SBTPHPCookieAdapter implements SBTCookieAdapter
{
	
	/**
	 * Retrieve the value from the cookie denoted by $index.
	 *
	 * @param string $index
	 * @return Cookie contents
	 */
	public function get($index)
	{
		return (isset($_COOKIE[$index]) ? $_COOKIE[$index] : null);
	}
	
	/**
	 * Sets the cookie. Cookie can only be set if the user is using https.
	 *
	 * @param string $sessionName
	 * @param string $value
	 * @param int $timestamp
	 */
	public function set($sessionName, $value, $timestamp)
	{
		setcookie($sessionName, $value, $timestamp, "", "", true, false); 
	}
}