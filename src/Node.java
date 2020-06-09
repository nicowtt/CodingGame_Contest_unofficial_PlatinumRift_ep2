 class Node {
    Integer id;
    Node parent;

    // constructor
     public Node() {};

     public Node(Integer id, Node parent) {
         this.id = id;
         this.parent = parent;
     }

     //getters and setters

     public Integer getId() {
         return id;
     }

     public void setId(Integer id) {
         this.id = id;
     }

     public Node getParent() {
         return parent;
     }

     public void setParent(Node parent) {
         this.parent = parent;
     }

     //to string

     @Override
     public String toString() {
         return "Node{" +
                 "id=" + id +
                 ", parent=" + parent +
                 '}';
     }
 }
