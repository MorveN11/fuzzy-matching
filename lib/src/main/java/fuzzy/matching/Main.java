package fuzzy.matching;

import fuzzy.matching.controller.UserComparator;
import fuzzy.matching.model.User;

public class Main {
    public static void main(String[] arr){
        User user1 = new User("1", "Alice", 25, "Female");
        User user2 = new User("2", "Bob", 30, "Male");


        UserComparator comparator = new UserComparator();
        double similarity = comparator.compareUser(user1, user2);
        System.out.println("Similarity between " + user1.getName() + " and " + user2.getName() + ": " + similarity);
    }
}
