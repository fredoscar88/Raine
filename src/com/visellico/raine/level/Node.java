package com.visellico.raine.level;

import com.visellico.raine.util.Vector2i;

public class Node {

	//........coordinates. VECTORS IN COMPSCI ARE DIFFERENT THAN MATH (TODO). From what I gather, scalable arrays. Or something. That can store stuff.
	public Vector2i tile;	//eugh
	public Node parent;
	public double fCost, gCost, hCost;	//plsToHalp. How much it costs to use a path, ish. Like a distance. But not quite. Shortest path (crow flies) not always one we can use/best
										//	Or even, imagine a water tile that slows you down, you can still USE that path but it may cost more.
	//hCost -> heuristic cost
	//gCost -> node-to-node cost
	//fCost -> Combination of the other two
	
	//I think these costs increase as we move down the path- so when a new node is accessed its costs represent the cost of the path up to that node
	//	gCost is sum of our node to node distances as thecherno just said.
	//	hCost is a direct line to our end-node- as bird flies. Remaining distance, but not remaining distance that we're traveling. An estimate since it's just the dstnce formula, not the
																																				// "real" distance that we actually walk
	
	public Node(Vector2i tile, Node parent, double gCost, double hCost) {
		this.tile = tile;	
		this.parent = parent;	//Our Node has.. another node, for tracing. Parent node is the node that we came from. The beginning node has a null parent.
		this.gCost = gCost;	
		this.hCost = hCost;	
		this.fCost = this.gCost + this.hCost;
	}
	
}
