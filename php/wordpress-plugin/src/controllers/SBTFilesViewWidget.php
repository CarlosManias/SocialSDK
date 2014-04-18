***REMOVED***
/*
 * © Copyright IBM Corp. 2014
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
 * Files view widget.
 *
 * @author Benjamin Jakobus
 */
class SBTFilesViewWidget extends SBTBaseGridWidget {
	
	protected $widget_id = 'ibm_sbtk_files_view_widget';
	protected $widget_name = 'IBM Files View';
	protected $widget_description = 'Use this widget to add an interactive files grid, allowing you to display and edit files from IBM SmartCloud for Social Business or IBM Connections on Premise.';
	protected $widget_location =  '/views/widgets/ibm-sbt-files-view.php';

	/**
	 * Constructor.
	 */
	public function __construct() {
		parent::__construct($this->widget_id, $this->widget_name, $this->widget_description, BASE_PATH . $this->widget_location);
	}
	
	/**
	 * Ouputs the options form on admin
	 *
	 * @param array $instance The widget options
	 */
	public function form($instance) {
		parent::form($instance);
		
		if (isset($instance['ibm-sbtk-files-type'])) {
			$type = $instance['ibm-sbtk-files-type'];
		} else {
			$type = "publicFiles";
		}
		
		if (isset($instance['ibm-sbtk-files-action-bar'])) {
			$actionBar = $instance['ibm-sbtk-files-action-bar'];
		} else {
			$actionBar = true;
		}
		?>
		<p>
			<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-files-action-bar'); ?>">***REMOVED*** echo $GLOBALS[LANG]['action-bar']?>:</label> 
			<input ***REMOVED*** echo ($actionBar ? 'checked="checked"' : ''); ?> id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-files-action-bar'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-files-action-bar'); ?>" type="checkbox" value="actionBar" />
		</p>
		<p>
			<label for="***REMOVED*** echo $this->get_field_id('ibm-sbtk-files-type'); ?>">***REMOVED*** echo $GLOBALS[LANG]['file-type']?>:</label> 
			<select id="***REMOVED*** echo $this->get_field_id('ibm-sbtk-files-type'); ?>" name="***REMOVED*** echo $this->get_field_name('ibm-sbtk-files-type'); ?>">
				<option ***REMOVED*** echo ($type == 'myFiles' ? 'selected="selected"' : ''); ?> value="myFiles">***REMOVED*** echo $GLOBALS[LANG]['my-files']?></option>
				<option ***REMOVED*** echo ($type == 'publicFiles' ? 'selected="selected"' : ''); ?> value="publicFiles">***REMOVED*** echo $GLOBALS[LANG]['public-files']?></option>
				<option ***REMOVED*** echo ($type == 'myPinnedFiles' ? 'selected="selected"' : ''); ?> value="myPinnedFiles">***REMOVED*** echo $GLOBALS[LANG]['pinned-files']?></option>
				<option ***REMOVED*** echo ($type == 'myFolders' ? 'selected="selected"' : ''); ?> value="myFolders">***REMOVED*** echo $GLOBALS[LANG]['my-folders']?></option>
				<option ***REMOVED*** echo ($type == 'publicFolders' ? 'selected="selected"' : ''); ?> value="publicFolders">***REMOVED*** echo $GLOBALS[LANG]['public-folders']?></option>
				<option ***REMOVED*** echo ($type == 'myPinnedFolders' ? 'selected="selected"' : ''); ?> value="myPinnedFolders">***REMOVED*** echo $GLOBALS[LANG]['my-pinned-folders']?></option>
				<option ***REMOVED*** echo ($type == 'activeFolders' ? 'selected="selected"' : ''); ?> value="activeFolders">***REMOVED*** echo $GLOBALS[LANG]['active-folders']?></option>
				<option ***REMOVED*** echo ($type == 'fileShares' ? 'selected="selected"' : ''); ?> value="fileShares">***REMOVED*** echo $GLOBALS[LANG]['file-shares']?></option>
				<option ***REMOVED*** echo ($type == 'communityFiles' ? 'selected="selected"' : ''); ?> value="communityFiles">***REMOVED*** echo $GLOBALS[LANG]['community-files']?></option>
			</select>
		</p>
		***REMOVED*** 
	}
	
	/**
	 * Processing widget options on save
	 *
	 * @param array $new_instance The new options
	 * @param array $old_instance The previous options
	 */
	public function update($new_instance, $old_instance) {
		$instance = parent::update($new_instance, $old_instance);
		$instance['ibm-sbtk-files-action-bar'] = (!empty($new_instance['ibm-sbtk-files-action-bar'])) ? strip_tags($new_instance['ibm-sbtk-files-action-bar'] ) : '';
		$instance['ibm-sbtk-files-type'] = (!empty($new_instance['ibm-sbtk-files-type'])) ? strip_tags($new_instance['ibm-sbtk-files-type'] ) : '';
		return $instance;
	}
	
	/**
	 * Outputs the content of the widget.
	 *
	 * @param array $args
	 * @param array $instance
	 */
	public function widget($args, $instance) {
		parent::widget($args, $instance);
	}

}
