package search;

import data.CNet;
import data.Bag;
import data.Item;
import java.util.ArrayList;
import java.util.HashMap;

public class Backtrack 
{
	public static boolean backtracking(ArrayList<Bag> bags, ArrayList<Item> items, HashMap<Item,Bag> assignment, CNet net)
	{
		if (items.size()==0)
		{
			int itemCount=0;
			float weight=0;
			for (int i=0; i<bags.size(); i++)
			{
				itemCount=bags.get(i).items.size();
				if (itemCount<net.lowerLim || itemCount>net.upperLim)
				{
					return false;
				}
				weight=0;
				for (int j=0; j<bags.get(i).items.size(); j++)
				{
					weight+=bags.get(i).items.get(j).weight;
				}
				if (weight/(weight+bags.get(i).size)<0.9) return false;
			}
			return true;
		}
		for (int i=0; i<items.size(); i++)
		{
			for (int j=0; j<bags.size(); j++)
			{
				boolean consistency=consistent(bags.get(j), items.get(i), assignment, net);
				//boolean weightCheck=bags.get(j).addItem(items.get(i));
				if (consistency)
				{
					bags.get(j).addItem(items.get(i));
					assignment.put(items.get(i), bags.get(j));
					ArrayList<Item> itemsCopy = deepCopy(items);
					ArrayList<Bag> bagsCopy = deepCopyBag(bags);
					itemsCopy.remove(i);
					if (backtracking(bagsCopy,itemsCopy,assignment,net))
					{
						return true;
					}
					bags.get(j).removeItem(items.get(i));
				}
				assignment.remove(items.get(i));
				
			}
			return false;
		}
		return false;
	}
	public static ArrayList<Item> deepCopy(ArrayList<Item> list)
	{
		ArrayList<Item> copy=new ArrayList<Item>();
		for(Item item:list)
		{
			Item copyItem=new Item(item.name, item.weight);
			copyItem.setValidBags(item.validBags);
			copy.add(copyItem);
		}
		return copy;
	}
	public static ArrayList<Bag> deepCopyBag(ArrayList<Bag> list)
	{
		ArrayList<Bag> copy=new ArrayList<Bag>();
		for(Bag bag:list)
		{
			Bag copyItem=new Bag(bag.name, bag.size);
			copyItem.setItems(bag.items);
			copy.add(copyItem);
		}
		return copy;
	}
	public static boolean consistent(Bag bag, Item item, HashMap<Item, Bag> assignments, CNet net)
	{
		return net.checkConsistency(item, bag, assignments);
	}
}
