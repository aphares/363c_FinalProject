/**
 * @author Marc Isaac (misaac34@iastate.edu)
 */
package cs363c;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "user")
public class User 
{
	private int followers, following;
	private String screen_name, name, sub_category, category, ofstate;
	
	public User(String screen_name, String name, String sub_category, String category, String ofstate, int followers, int following)
	{
		this.screen_name = screen_name;
		this.name = name;
		this.sub_category = sub_category;
		this.category = category;
		this.ofstate = ofstate;
		this.followers = followers;
		this.following = following;
	}
	
	public User()
	{
		//Empty constructor is required by Hibernate
	}
	
	@Id
    @GeneratedValue
    @Column(name = "screen_name", unique = true, nullable = false)
	public String getScreenname()
	{
		return this.screen_name;
	}
	public void setScreenname(String screen)
	{
		this.screen_name = screen;
	}
	
	@Column(name = "name")
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Column(name = "sub_category", unique = false, nullable = true)
	public String getSubcat()
	{
		return this.sub_category;
	}
	public void setSubcat(String subcat)
	{
		this.sub_category = subcat;
	}
	
	@Column(name = "category", unique = false, nullable = true)
	public String getCatat()
	{
		return this.category;
	}
	public void setCat(String Cat)
	{
		this.category = Cat;
	}
	
	@Column(name = "ofstate")
	public String getState()
	{
		return this.ofstate;
	}
	public void setState(String state)
	{
		this.ofstate = state;
	}
	
	@Column(name = "followers")
	public int getFollowers()
	{
		return this.followers;
	}
	public void setFollowers(int followers)
	{
		this.followers = followers;
	}
	
	@Column(name = "following")
	public int getFollowing()
	{
		return this.following;
	}
	public void setFollowing(int following)
	{
		this.following = following;
	}
	
	
}
