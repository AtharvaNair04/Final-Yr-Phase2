import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    int n = Integer.parseInt(sc.next());
    int m = Integer.parseInt(sc.next());
    int k = Integer.parseInt(sc.next());
    UserService userService = new UserService();
    for (int i = 1; i < n + 1; i++) {
      userService.addUser(new User(i));
    }
    for (int i = 0; i < m; i++) {
      userService.makeFriends(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()));
    }
    for (int i = 0; i < k; i++) {
      userService.makeBlocked(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()));
    }
    pw.println(
      userService.countCandidates().stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "))
    );
  }

  static class User {

    private int id;
    Set<User> friends = new HashSet<>();
    Set<User> blocked = new HashSet<>();

    public User(int id) {
      this.id = id;
    }

    void addFriends(User user) {
      friends.add(user);
    }

    void visitFriendsChain(Set<User> visitor) {
      Deque<User> dfs = new LinkedList<>();
      dfs.add(this);
      while (!dfs.isEmpty()) {

      }
      for (User friend : friends) {
        if (!visitor.contains(friend)) {
          visitor.add(friend);
          friend.visitFriendsChain(visitor);
        }
      }
    }

    Set<User> resolveGroup() {
      Set<User> group = new HashSet<>();
      group.add(this);
      Deque<User> dfs = new LinkedList<>();
      dfs.add(this);
      while (!dfs.isEmpty()) {
        User u = dfs.removeFirst();
        for (User f : u.friends) {
          if (!group.contains(f)) {
            dfs.addFirst(f);
            group.add(f);
          }
        }
      }
      return group;
    }

    Set<User> friendsChain(Set<User> current) {
      Set<User> newFriends = new HashSet<>(friends);
      newFriends.removeAll(current);
      current.addAll(newFriends);
      Set<User> newFriendsChain = newFriends.stream()
        .map(u -> u.friendsChain(current))
        .flatMap(Set::stream)
        .collect(Collectors.toSet());
      newFriendsChain.remove(this);
      current.addAll(newFriendsChain);
      return current;
    }

    boolean isFriend(User u) {
      return friends.contains(u);
    }

    void addBlocked(User user) {
      blocked.add(user);
    }

    boolean isBlocked(User u) {
      return blocked.contains(u);
    }

    @Override
    public boolean equals(Object o) {
      return this == o;
    }

    @Override
    public int hashCode() {
      return id;
    }
  }

  static class UserService {

    LinkedHashMap<Integer, User> users = new LinkedHashMap<>();

    void addUser(User user) {
      users.put(user.id, user);
    }

    void makeFriends(int a, int b) {
      User userA = users.get(a);
      User userB = users.get(b);
      userA.addFriends(userB);
      userB.addFriends(userA);
    }

    void makeBlocked(int a, int b) {
      User userA = users.get(a);
      User userB = users.get(b);
      userA.addBlocked(userB);
      userB.addBlocked(userA);
    }

    List<Integer> countCandidates() {
      Map<User, Set<User>> chainedUserGroupCache = new HashMap<>();
      for (User user : users.values()) {
        if (chainedUserGroupCache.containsKey(user)) {
          continue;
        }
        Set<User> userGroup = user.resolveGroup();
        for (User groupUser : userGroup) {
          chainedUserGroupCache.put(groupUser, userGroup);
        }
      }
      List<Integer> answer = new ArrayList<>();
      for (User user : users.values()) {
        Set<User> candidate = chainedUserGroupCache.get(user);
        int ans = candidate.size();
        ans -= user.friends.size();
        for (User b : user.blocked) {
          if (candidate.contains(b)) {
            ans--;
          }
        }
        answer.add(ans - 1);
      }
      return answer;
    }
  }
}
