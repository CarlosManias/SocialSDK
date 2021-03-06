/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.blogs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.BlogPostsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.BlogsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.CommentsFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.feedhandler.TagFeedHandler;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;
import com.ibm.sbt.services.client.connections.blogs.transformers.BaseBlogTransformer;
import com.ibm.sbt.services.client.connections.forums.ForumServiceException;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * BlogService
 * 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class BlogService extends BaseService {
	
	public String contextRoot = "blogs";
	public String defaultHomepageHandle = "homepage";
	public static String BLOG_HOMEPAGE_KEY = "blogHomepageHandle";
	
	/**
	 * Constructor Creates BlogService Object with default endpoint
	 */
	public BlogService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates BlogService Object with the specified endpoint
	 */
	public BlogService(String endpoint) {
		super(endpoint);
		this.setHomepageFromEndpoint(this.getEndpoint());
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates BlogService Object with the specified endpoint
	 */
	public BlogService(Endpoint endpoint) {
		super(endpoint);
		this.setHomepageFromEndpoint(this.getEndpoint());
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "blogs";
	}
	
	private void setHomepageFromEndpoint(Endpoint endpoint){
		Map<String, String> serviceMap = endpoint.getServiceMappings();
		if(serviceMap != null){
			String homepage = serviceMap.get(BLOG_HOMEPAGE_KEY);
			if(StringUtil.isNotEmpty(homepage)){
				this.defaultHomepageHandle = homepage;				
			}
		}
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @deprecated use getAllBlogs instead
	 * @return
	 * @throws ForumServiceException
	 */
	public BlogList getBlogs() throws BlogServiceException{
		return getAllBlogs();
		
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @return
	 * @throws ForumServiceException
	 */
	public BlogList getAllBlogs() throws BlogServiceException{
		return getAllBlogs(null);
		
	}
	
	/**
	 * This method returns the all blogs
	 * 
	 * @param parameters
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getAllBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String blogsUrl = BlogUrls.ALL_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogs = (BlogList)getEntities(blogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}
	
	
	/**
	 * This method returns My blogs
	 * 
	 * @return BlogList
	 * @throws ForumServiceException
	 */
	public BlogList getMyBlogs() throws BlogServiceException{
		return getMyBlogs(null);
		
	}
	
	/**
	 * This method returns My blogs
	 * 
	 * @param parameters
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getMyBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String myBlogsUrl = BlogUrls.MY_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogs = (BlogList)getEntities(myBlogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}

	/**
	 * This method returns the featured blogs
	 * 
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getFeaturedBlogs() throws BlogServiceException{
		return getFeaturedBlogs(null);
		
	}
	
	/**
	 * This method returns the featured blogs
	 * 
	 * @param parameters
	 * @return BlogList
	 * @throws BlogServiceException
	 */
	public BlogList getFeaturedBlogs(Map<String, String> parameters) throws BlogServiceException {
		BlogList blogs;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String featuredBlogsUrl = BlogUrls.FEATURED_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogs = (BlogList)getEntities(featuredBlogsUrl, parameters, new BlogsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogs;
	}
	/**
	 * This method returns the most recent Blog posts
	 * 
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getAllPosts() throws BlogServiceException{
		return getAllPosts(null);
		
	}
	
	/**
	 * This method returns the most recent posts
	 * 
	 * @param parameters
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getAllPosts(Map<String, String> parameters) throws BlogServiceException {
		BlogPostList blogPosts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestPostsUrl = BlogUrls.ALL_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogPosts = (BlogPostList)getEntities(latestPostsUrl, parameters, new BlogPostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogPosts;
	}
	
	/**
	 * This method returns the featured posts
	 * 
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getFeaturedPosts() throws BlogServiceException{
		return getFeaturedPosts(null);
		
	}
	
	/**
	 * This method returns the featured posts
	 * 
	 * @param parameters
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getFeaturedPosts(Map<String, String> parameters) throws BlogServiceException {
		BlogPostList blogPosts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String featuredPostsUrl = BlogUrls.ALL_FEATURED_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogPosts = (BlogPostList)getEntities(featuredPostsUrl, parameters, new BlogPostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogPosts;
	}
	
	/**
	 * This method returns the recommended posts
	 * 
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getRecommendedPosts() throws BlogServiceException{
		return getRecommendedPosts(null);
		
	}
	
	/**
	 * This method returns the recommended posts
	 * 
	 * @param parameters
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getRecommendedPosts(Map<String, String> parameters) throws BlogServiceException {
		BlogPostList blogPosts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String recommendedPostsUrl = BlogUrls.ALL_RECOMMENDED_BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			blogPosts = (BlogPostList)getEntities(recommendedPostsUrl, parameters, new BlogPostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return blogPosts;
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getAllComments() throws BlogServiceException{
		return getAllComments(null);
		
	}
	
	/**
	 * This method returns the latest comments on blogs
	 * 
	 * @param parameters
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getAllComments(Map<String, String> parameters) throws BlogServiceException {
		CommentList comments;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String commentsUrl = BlogUrls.ALL_BLOG_COMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			comments = (CommentList)getEntities(commentsUrl, parameters, new CommentsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return comments;
	}
	
	/**
	 * This method returns the tags on all blogs
	 * 
	 * @return TagList
	 * @throws BlogServiceException
	 */
	public TagList getAllTags() throws BlogServiceException {
		TagList tags;
		try {
			String allTagsUrl = BlogUrls.ALL_BLOG_TAGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			tags = (TagList)getEntities(allTagsUrl, null, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return tags;
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getBlogPosts(String blogHandle) throws BlogServiceException{
		return getBlogPosts(blogHandle, null);
		
	}
	
	/**
	 * This method returns the most recent posts for a particular Blog
	 * 
	 * @param blogHandle
	 * @param parameters
	 * @return BlogPostList
	 * @throws BlogServiceException
	 */
	public BlogPostList getBlogPosts(String blogHandle, Map<String, String> parameters) throws BlogServiceException {
		BlogPostList posts;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestPostsUrl = BlogUrls.BLOG_POSTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			posts = (BlogPostList)getEntities(latestPostsUrl, parameters, new BlogPostsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return posts;
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @param blogHandle
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogComments(String blogHandle) throws BlogServiceException{
		return getBlogComments(blogHandle, null);
	}
	
	/**
	 * This method returns the latest comments for a particular Blog
	 * 
	 * @param blogHandle
	 * @param parameters
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getBlogComments(String blogHandle, Map<String, String> parameters) throws BlogServiceException {
		CommentList comments;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String latestCommentsUrl = BlogUrls.BLOG_COMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			comments = (CommentList)getEntities(latestCommentsUrl, parameters, new CommentsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return comments;
	}
	
	/**
	 * This method returns the comments for a particular Blog entry
	 * 
	 * @param blogHandle
	 * @param entryAnchor
	 * @param parameters
	 * @return CommentList
	 * @throws BlogServiceException
	 */
	public CommentList getEntryComments(String blogHandle, String entryAnchor, Map<String, String> parameters) throws BlogServiceException {
		CommentList comments;
		
		if(null == parameters){
			parameters = new HashMap<String, String>();
		}
		try {
			String url = BlogUrls.BLOG_ENTRYCOMMENTS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			comments = (CommentList)getEntities(url, parameters, new CommentsFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return comments;
	}
	
	/**
	 * This method returns the tags for a particular blog
	 * 
	 * @param blogHandle
	 * @return TagList
	 * @throws BlogServiceException
	 */
	public TagList getBlogTags(String blogHandle) throws BlogServiceException {
		TagList tags;
		try {
			String allTagsUrl = BlogUrls.BLOG_TAGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			tags = (TagList)getEntities(allTagsUrl, null, new TagFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new BlogServiceException(e);
		} catch (IOException e) {
			throw new BlogServiceException(e);
		}
		return tags;
	}
	
	/**
	 * Wrapper method to create a Blog
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param Blog
	 * @return Blog
	 * @throws BlogServiceException
	 */
	public Blog createBlog(Blog blog) throws BlogServiceException {
		if (null == blog){
			throw new BlogServiceException(null,"null blog");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String createBlogUrl = BlogUrls.MY_BLOGS.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle));
			result = createData(createBlogUrl, null, headers, payload);
			blog = (Blog) new BlogsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog");
		}

        return blog;
	}
	/**
	 * Wrapper method to get a Blog 
	 * 
	 * @param Blog
	 * @throws BlogServiceException
	 */
	public Blog getBlog(String blogUuid) throws BlogServiceException {
	
		if(StringUtil.isEmpty(blogUuid)){
			throw new BlogServiceException(null, "null blogUuid");
		}
		String getBlogUrl = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blogUuid));
		Blog blog;
		try {
			blog = (Blog)getEntity(getBlogUrl, null, new BlogsFeedHandler(this));
		} catch (Exception e) {
			throw new BlogServiceException(e, "error getting blog");
		} 
		return blog;
		
	}
	/**
	 * Wrapper method to update a Blog 
	 * 
	 * @param Blog
	 * @throws BlogServiceException
	 */
	public void updateBlog(Blog blog) throws BlogServiceException {
		if (null == blog){
			throw new BlogServiceException(null,"null blog");
		}
		try {
			if(blog.getFieldsMap().get(AtomXPath.title)== null)
				blog.setTitle(blog.getTitle());
			if(blog.getFieldsMap().get(AtomXPath.summary)== null)
				blog.setSummary(blog.getSummary());
			if(!blog.getFieldsMap().toString().contains(BlogXPath.tags.toString()))
				blog.setTags(blog.getTags());

			BaseBlogTransformer transformer = new BaseBlogTransformer(blog);
			Object 	payload = transformer.transform(blog.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updateBlogUrl = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blog.getUid()));
			// not using super.updateData, as unique id needs to be provided, along with passing params, since no params
			//is passed, it'll throw NPE in BaseService updateData - check with Manish
			getClientService().put(updateBlogUrl, null,headers, payload,ClientService.FORMAT_NULL);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error updating blog");
		}

	}
	
	/**
	 * Wrapper method to delete a post
	 * <p>
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				blogUuid which is to be deleted
	 * @throws BlogServiceException
	 */
	public void removeBlog(String blogUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogUuid)){
			throw new BlogServiceException(null, "null blog Uuid");
		}
		try {
			String deleteBlogUrl = BlogUrls.GET_UPDATE_REMOVE_BLOG.format(this, BlogUrlParts.blogHandle.get(defaultHomepageHandle), BlogUrlParts.entryAnchor.get(blogUuid));
			getClientService().delete(deleteBlogUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e,"error deleting blog");
		} 	
		
	}
	
	/**
	 * Wrapper method to get a particular Blog Post
	 * <p>
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws BlogServiceException
	 */
	public BlogPost getBlogPost(String blogHandle, String postUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new BlogServiceException(null,"postUuid is null");
		}
		BlogPost blogPost;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("entryid", postUuid);
			String getPostUrl = BlogUrls.BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle));
			blogPost = (BlogPost)getEntity(getPostUrl, params, new BlogPostsFeedHandler(this));
			
		} catch (Exception e) {
			throw new BlogServiceException(e, "error getting blog post");
		}
        return blogPost;
	}
	/**
	 * Wrapper method to recommend/like a Blog Post
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws BlogServiceException
	 */
	public void recommendPost(String blogHandle, String postUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new BlogServiceException(null,"postUuid is null");
		}
		try {
			String recommendPostUrl = BlogUrls.RECOMMEND_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			super.createData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error recommending blog post");
		}
		
	}
	/**
	 * Wrapper method to unrecommend/unlike a Blog Post
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param blogHandle
	 * @param postUuid
	 * @return BlogPost
	 * @throws BlogServiceException
	 */
	public void unrecommendPost(String blogHandle, String postUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(postUuid)){
			throw new BlogServiceException(null,"postUuid is null");
		}
		try {
			String recommendPostUrl = BlogUrls.RECOMMEND_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			super.deleteData(recommendPostUrl, null, null);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error unrecommending blog post");
		}
	}
	/**
	 * Wrapper method to create a Blog Post
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * @param BlogPost
	 * @param blogHandle
	 * @return BlogPost
	 * @throws BlogServiceException
	 */
	public BlogPost createBlogPost(BlogPost post, String blogHandle) throws BlogServiceException {
		if (null == post){
			throw new BlogServiceException(null,"null post");
		}
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is not passed");
		}
		Response result = null;
		try {
			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createPostUrl = BlogUrls.CREATE_BLOG_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle));
			result = createData(createPostUrl, null, headers, payload);
			post = (BlogPost) new BlogPostsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog post");
		}
		
        return post;
	}
	
	/**
	 * Wrapper method to update a Blog Post
	 * 
	 * @param BlogPost
	 * @param blogHandle
	 * @throws BlogServiceException
	 */
	public BlogPost updateBlogPost(BlogPost post, String blogHandle) throws BlogServiceException {
		if (null == post){
			throw new BlogServiceException(null,"null post");
		}
		try {
			if(post.getFieldsMap().get(AtomXPath.title)== null)
				post.setTitle(post.getTitle());
			if(post.getFieldsMap().get(AtomXPath.content)== null)
				post.setContent(post.getContent());
			if(!post.getFieldsMap().toString().contains(BlogXPath.tags.toString()))
				post.setTags(post.getTags());

			BaseBlogTransformer transformer = new BaseBlogTransformer(post);
			Object 	payload = transformer.transform(post.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String updatePostUrl = BlogUrls.UPDATE_REMOVE_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(post.getUid()));
			Response result = getClientService().put(updatePostUrl, null,headers, payload,ClientService.FORMAT_NULL);
			post = (BlogPost) new BlogPostsFeedHandler(this).createEntity(result);


		} catch (Exception e) {
			throw new BlogServiceException(e, "error updating blog post");
		}

		return post;
	}
	
	/**
	 * Wrapper method to delete a post
	 * <p>
	 * User should be logged in as a owner of the Blog to call this method.
	 * 
	 * @param String
	 * 				postUuid which is to be deleted
	 * @param blogHandle
	 * @throws BlogServiceException
	 */
	public void removeBlogPost(String postUuid, String blogHandle) throws BlogServiceException {
		if (StringUtil.isEmpty(postUuid)){
			throw new BlogServiceException(null, "null post id");
		}
		try {
			String deletePostUrl = BlogUrls.UPDATE_REMOVE_POST.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(postUuid));
			getClientService().delete(deletePostUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e,"error deleting post");
		} 	
	}
	

	/**
	 * Wrapper method to get a particular Blog Comment
	 * <p>
	 * 
	 * @param blogHandle
	 * @param commentUuid
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public Comment getBlogComment(String blogHandle, String commentUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentUuid)){
			throw new BlogServiceException(null,"commentUuid is null");
		}
		Comment comment;
		try {
			String getCommentUrl = BlogUrls.GET_REMOVE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(commentUuid));
			comment = (Comment)getEntity(getCommentUrl, null, new CommentsFeedHandler(this));
		} catch (Exception e) {
			throw new BlogServiceException(e, "error getting blog comment");
		}
        return comment;
	}
	
	/**
	 * Wrapper method to create a Blog Comment
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Comment
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public Comment createBlogComment(Comment comment, String blogHandle, String postUuid) throws BlogServiceException {
		if (null == comment){
			throw new BlogServiceException(null,"null comment");
		}
		Response result = null;
		try {
			comment.setPostUuid(postUuid);
			BaseBlogTransformer transformer = new BaseBlogTransformer(comment);
			
			Object 	payload = transformer.transform(comment.getFieldsMap());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String createCommentUrl = BlogUrls.CREATE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(""));
			
			result = createData(createCommentUrl, null, headers, payload);
			comment = (Comment) new CommentsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new BlogServiceException(e, "error creating blog comment");
		}

        return comment;
	}
	
	/**
	 * Wrapper method to remove a particular Blog Comment
	 * <p>
	 * 
	 * @param blogHandle
	 * @param commentUuid
	 * @return Comment
	 * @throws BlogServiceException
	 */
	public void removeBlogComment(String blogHandle, String commentUuid) throws BlogServiceException {
		if (StringUtil.isEmpty(blogHandle)){
			throw new BlogServiceException(null,"blog handle is null");
		}
		if (StringUtil.isEmpty(commentUuid)){
			throw new BlogServiceException(null,"commentUuid is null");
		}
		try {
			String getCommentUrl = BlogUrls.GET_REMOVE_COMMENT.format(this, BlogUrlParts.blogHandle.get(blogHandle), BlogUrlParts.entryAnchor.get(commentUuid));
			getClientService().delete(getCommentUrl);
		} catch (Exception e) {
			throw new BlogServiceException(e, "error deleting blog comment");
		}
	}
	/*
	 * Util methods
	 */
	
	/**
	 * 
	 * @return The blog homepage currently being used in requests to the blog service.
	 */
	public String getHomepageHandle() {
		return defaultHomepageHandle;
	}
	
    /**
     * Change the blog homepage, to be used in all subsequent requests for this service instance. 
     * This follows /blogs in the connections url, and is 'homepage' by default. 
     * 
     * e.g. CONNECTIONS_URL/blogs/homepage
     * 
     * @param defaultHandle
     */
	public void setHomepageHandle(String defaultHandle) {
		this.defaultHomepageHandle = defaultHandle;
	}

}
