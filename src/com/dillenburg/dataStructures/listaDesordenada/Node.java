package com.dillenburg.dataStructures.listaDesordenada;

public class Node<X> {
    protected X  info;
    protected Node<X> next;
    protected Node<X> prev;

    public X getInfo() {
        return this.info;
    }

    public Node<X> getNext() {
        return this.next;
    }

    public void setInfo(X x) {
        this.info = x;
    }

    public void setNext(Node<X> n) {
        this.next = n;
        this.next.prev = this;
    }

    public Node(X x, Node<X> n) {
        this.info = x;
        this.next = n;
    }

    public Node(X x) {
        this(x,null);
    }

    public int hashCode() {
        int ret = 44;

        ret = ret*7 + info.hashCode();

        if (next != null)
            ret = ret*7 + next.hashCode();

        return ret;
    }

    public boolean equals (Object obj) {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (!this.getClass().equals(obj.getClass()))
            return false;

        Node node = (Node)obj;

        if (!(this.info).equals(node.info))
            return false;

        if (this.next == null)
            if (node.next == null)
                return true;
            else
                return false;

        if (!(this.next).equals(node.next))
            return false;

        return true;
    }
}