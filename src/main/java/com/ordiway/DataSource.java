package com.ordiway;

public class DataSource {
	private String url;
	private String user;
	
	public DataSource(String url, String user){
		this.url=url;
		this.user=user;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataSource))
            return false;
        DataSource other = (DataSource) o;
        return other.url.equals(url) &&
               other.user.equals(user);
    }
}