package hello;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String name;

	public void setID(int id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public int getID(){
		return id;
	}
	public String getName(){
		return name;
	}
	@Override
	public String toString(){
		return "ID: "+id+" Name:"+name;
	}
}