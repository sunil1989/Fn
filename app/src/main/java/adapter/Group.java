package adapter;

import java.util.ArrayList;

import data.FetchData;


public class Group {

    private String Name;
    private ArrayList<FetchData> Items;
    
    public Group(){
    	
    }

    public Group(String string) {
		// TODO Auto-generated constructor stub
    	this.Name=string;
	}

	public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<FetchData> getItems() {
        return Items;
    }

    public void setItems(ArrayList<FetchData> Items) {
        this.Items = Items;
    }

}