package utils;

import java.util.Iterator;
import java.util.Map.Entry;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import bdd.BDD;

public class GraphPainter {
	public static void draw(BDD drawee) {
		Graph g = new SingleGraph("BDD");
		g.addAttribute("ui.stylesheet", "node {text-size: 14; text-background-mode: rounded-box; text-background-color: blue;} edge.low{ stroke-mode: dots; stroke-color: red; stroke-width: 3; fill-color: red;} node.term{shape: cross; fill-color: grey; size: 15px, 15px;}");
		g.addNode("start").addAttribute("ui.hide", "");
		g.addNode(drawee.getTerminalFalse().hashCode() + "").addAttribute("ui.label", 0);
		g.addNode(drawee.getTerminalTrue().hashCode() + "").addAttribute("ui.label", 1);
		g.getNode(drawee.getTerminalFalse().hashCode() + "").addAttribute("ui.class", "term, node");
		g.getNode(drawee.getTerminalTrue().hashCode() + "").addAttribute("ui.class", "term, node");
		g.getNode(drawee.getTerminalFalse().hashCode() + "").addAttribute("xyz", 100, 100, 100);
		g.getNode(drawee.getTerminalTrue().hashCode() + "").addAttribute("xyz", 100, 100, 100);
		Iterator<Entry<Triple<Integer, Node, Node>, Node>> it = drawee.getLookupTable().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Triple<Integer, Node, Node>, Node> cur = (Entry<Triple<Integer, Node, Node>, Node>) it.next();
			g.addNode(cur.getValue().hashCode() + "");
			g.getNode(cur.getValue().hashCode() + "").addAttribute("ui.label", "x" + cur.getValue().getVar());
		}
		g.addEdge("start-edge", "start", drawee.getRoot().hashCode() + "", true).addAttribute("ui.label", "Start");
		//g.getEdge("start-edge").addAttribute("ui.style", "fill-color: grey");
		it = drawee.getLookupTable().entrySet().iterator();
		while (it.hasNext()){ 
			Entry<Triple<Integer, Node, Node>, Node> cur = (Entry<Triple<Integer, Node, Node>, Node>) it.next();
			g.addEdge(cur.getValue().hashCode() + cur.getKey().getSecond().hashCode() + "",
					cur.getValue().hashCode() + "", cur.getKey().getSecond().hashCode() + "", true);
			g.addEdge(cur.getValue().hashCode() + cur.getKey().getThird().hashCode() + "",
					cur.getValue().hashCode() + "", cur.getKey().getThird().hashCode() + "");
			g.getEdge(cur.getValue().hashCode() + cur.getKey().getSecond().hashCode() + "").addAttribute("ui.class", "low, edge", true);
		}
		g.display();
	}
}
