import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;

class InvalidInformationException extends Exception {
    InvalidInformationException(String msg) {
        super(msg);
    }
}

class MaxHeap //for priority queue which will be used for recommendations having more number of mutuals or common interests to be shown first
{
    ArrayList<Recommendation> heap = new ArrayList<>();

    MaxHeap() {
//index 0 stores the total number of elements in the max heap
        heap.add(new Recommendation(-1, 0, null));
    }

    //Insert a new recommendation
    void insert(Recommendation r) {
        heap.add(r); //the recommendation will be added at the last of the heap (elements will have index starting from 1 where root element will have index as 1)
        heap.get(0).score = heap.get(0).score + 1; //increase the count when a new element is added
        shiftUp(heap.get(0).score);
    }

    Recommendation extractMax() {
        if (heap.isEmpty()) {
            return null;
        }
        Recommendation max = heap.get(1); //the root element is the maximum element
        if (!heap.isEmpty()) {
            heap.set(1, heap.get(heap.get(0).score)); //the last element is set as the root
        }
        heap.remove(heap.get(0).score);
        heap.get(0).score = heap.get(0).score - 1; //decrement by 1 as one element is removed now
        shiftDown(1); //the element will be moved down if it is less than its child elements
        return max;
    }

    //as it is a max heap, the order of elements should be from highest to lowest i.e. the parent element is greater than the child element
    void shiftUp(int i) {
        while (i > 1) {
            int parent = i / 2;
            if (heap.get(parent).score >= heap.get(i).score) {
                break; //correct position of the element is found
            }
            swap(parent, i);
            i = parent;
        }
    }

    //as it is a max heap, the order of elements should be from highest to lowest i.e. the parent element is greater than the child element
    void shiftDown(int i) {
        while (true) {
            int left = 2 * i;
            int right = 2 * i + 1;
            int largest = i;
            if (left <= heap.get(0).score && heap.get(left).score > heap.get(i).score) {
                largest = left;
            }
            if (right <= heap.get(0).score && heap.get(right).score > heap.get(largest).score) {
                largest = right;
            }
            if (largest == i) {
                break;
            }
            swap(i, largest);
            i = largest;
        }
    }

    void swap(int i, int j) {
        Recommendation temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    boolean isEmpty() {
        if (heap.get(0).score == 0) {
            return true;
        }
        return false;
    }
}

class Recommendation {
    int candidateId, score;
    HashSet<Integer> recommendations = new HashSet<>();

    Recommendation(int candidateId, int score, HashSet<Integer> recommendations) {
        this.candidateId = candidateId;
        this.score = score;
        this.recommendations = recommendations;
    }
}

class User {
    Scanner sc = new Scanner(System.in);
    int id;
    String email, password, name, dob, college, bio, profSection;
    ArrayList<Integer> interests = new ArrayList<>();
    String currentNote;

    void signup() {
        System.out.println("Fill your data ");

// NAME
        while (true) {
            System.out.print("Enter name: ");
            name = sc.nextLine();
            if (name.matches("[a-zA-Z ]+"))
                break;
            System.out.println("Invalid name! Only letters and spaces allowed.");
        }

// EMAIL
        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                break;
            System.out.println("Invalid email format!");
        }

// PASSWORD
        while (true) {
            System.out.print("Enter Password (min 8 chars): ");
            password = sc.next();
            if (password.length() >= 8)
                break;
            System.out.println("Password must be at least 8 characters long.");
        }

// DOB
        while (true) {
            System.out.print("Enter Date of Birth (DD-MM-YYYY): ");
            dob = sc.next();
            if (dob.matches("^\\d{2}-\\d{2}-\\d{4}$"))
                break;
            System.out.println("Invalid format! Use DD-MM-YYYY.");
        }

// COLLEGE
        int coll;
        while (true) {
            System.out.println("\nSelect College from one of the below");
            System.out.println("1.CCOEW");
            System.out.println("2.COEP");
            System.out.println("3.PICT");
            System.out.println("4.PVG");
            System.out.println("5.VIT");

            try {
                coll = sc.nextInt();
                if (coll > 0 && coll < 6)
                    break;

            } catch (InputMismatchException e) {
                System.out.println("Enter valid option");
                sc.next();
            }

        }
        switch (coll) {
            case 1:
                college = "CCOEW";
                break;

            case 2:
                college = "COEP";
                break;

            case 3:
                college = "PICT";
                break;

            case 4:
                college = "PVG";
                break;

            case 5:
                college = "VIT";
                break;

            default:
                System.out.println("Invalid input.");
        }

// BIO
        sc.nextLine();
        System.out.print("Enter your bio: ");
        bio = sc.nextLine();

// INTERESTS
        System.out.println(
                "Select your interests:\n1. AI\n2. Web Development\n3. App Development\n4. Robotics\n5. Music\n6. Photography\n7. Gaming\n8. Reading\n9. Sports\n10. ML");

        int l;
        while (true) {
            System.out.print("Input how many interests you want to select (1–10): ");
            try {
                l = sc.nextInt();
                if (l >= 1 && l <= 10)
                    break;
                System.out.println("Enter a valid number between 1 and 10.");
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid number between 1 and 10.");
            }
        }

        System.out.print("Input your interests (enter numbers separated by spaces): ");
        for (int i = 0; i < l; i++) {
            int val = sc.nextInt();
            if (val >= 1 && val <= 10)
                interests.add(val);
            else
                System.out.println(" Invalid interest number ignored: " + val);
        }

        sc.nextLine();

// PROFESSIONAL SECTION
        while (true) {
            System.out.print("Enter about your professional experience (If not, enter 'NA'): ");
            profSection = sc.nextLine();
            if (profSection.matches("[a-zA-Z ]+"))
                break;
            System.out.println("Invalid input! Only letters and spaces allowed.");
        }

        System.out.println("\n Signup successful!");
//FileHandler
    }


    User login() {
        System.out.println("Enter your email id: ");
        String lEmail = sc.nextLine();
        System.out.println("Enter your password: ");
        String lPassword = sc.nextLine();
        for (User u : FileHandler.usersMap.values()) {
            if (u.email.equals(lEmail) && u.password.equals(lPassword)) {
                return u;
            }
        }
        return null;
    }

    void updateProfile() {
        int choice;
        do {
            while (true) {
                try {
                    System.out.println("\n--- Update Profile Menu ---");
                    System.out.println("1. Update Name");
                    System.out.println("2. Update Email");
                    System.out.println("3. Update Password");
                    System.out.println("4. Update Date of Birth");
                    System.out.println("5. Update College");
                    System.out.println("6. Update Bio");
                    System.out.println("7. Update Interests");
                    System.out.println("8. Update Professional Section");
                    System.out.println("0. Back to Main Menu");
                    System.out.print("Enter your choice: ");
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input.");
                    sc.next();
                }
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine().trim();
                    if (newName.matches("[a-zA-Z ]+")) {
                        this.name = newName;
                        System.out.println("Name updated successfully!");
                    } else {
                        System.out.println("Invalid name. ");
                    }
                }

                case 2 -> {
                    System.out.print("Enter new email: ");
                    String newEmail = sc.nextLine().trim();
                    if (newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        this.email = newEmail;
                        System.out.println("Email updated successfully!");
                    } else {
                        System.out.println("Invalid email format.");
                    }
                }

                case 3 -> {
                    System.out.print("Enter current password: ");
                    String oldPass = sc.nextLine();

                    if (!this.password.equals(oldPass)) {
                        System.out.println("Incorrect password. Cannot update.");
                        break;
                    }

                    System.out.print("Enter new password: ");
                    this.password = sc.nextLine();
                    System.out.println("Password updated successfully!");
                    break;

                }

                case 4 -> {
                    System.out.print("Enter new date of birth (DD-MM-YYYY): ");
                    String newDob = sc.nextLine().trim();
                    if (newDob.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        this.dob = newDob;
                        System.out.println("Date of Birth updated successfully!");
                    } else {
                        System.out.println("Invalid format. Use DD-MM-YYYY.");
                    }
                }

                case 5 -> {
                    int coll;
                    while (true) {
                        System.out.println("\nSelect College from one of the below");
                        System.out.println("1.MKSSS's Cummins");
                        System.out.println("2.COEP");
                        System.out.println("3.PICT");
                        System.out.println("4.PVG");
                        System.out.println("5.VIT");
                        try {
                            coll = sc.nextInt();
                            if (coll > 0 && coll < 6)
                                break;
                        } catch (InputMismatchException e) {
                            System.out.println("Enter valid option");
                            sc.next();
                        }
                    }
                    switch (coll) {
                        case 1:
                            college = "CCOEW";
                            break;
                        case 2:
                            college = "COEP";
                            break;
                        case 3:
                            college = "PICT";
                            break;
                        case 4:
                            college = "PVG";
                            break;
                        case 5:
                            college = "VIT";
                            break;
                        default:
                            System.out.println("Invalid input.");
                    }


                }

                case 6 -> {
                    System.out.print("Enter new bio: ");
                    this.bio = sc.nextLine();
                    System.out.println("Bio updated successfully!");
                }

                case 7 -> {
                    this.interests.clear();
                    System.out.println("Select your new interests (enter numbers separated by spaces):");
                    System.out.println(
                            "1. AI\n2. Web Development\n3. App Development\n4. Robotics\n5. Music\n6. Photography\n7. Gaming\n8. Reading\n9. Sports\n10. ML");
                    String[] parts = sc.nextLine().trim().split(" ");
                    for (String p : parts) {
                        try {
                            int num = Integer.parseInt(p);
                            if (num >= 1 && num <= 10)
                                this.interests.add(num);
                            else
                                System.out.println("Ignored invalid interest: " + num);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input ignored: " + p);
                        }
                    }
                    System.out.println("Interests updated successfully!");
                }

                case 8 -> {
                    System.out.print("Enter new professional experiences (or NA): ");
                    String newProf = sc.nextLine().trim();
                    if (newProf.matches("[a-zA-Z ]+")) {
                        this.profSection = newProf;
                        System.out.println("Professional section updated successfully!");
                    } else {
                        System.out.println("Invalid input.");
                    }
                }

                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Try again.");
            }

// Save immediately to file
            FileHandler.usersMap.put(this.id, this);
            FileHandler.saveProfiles();

        } while (choice != 0);
    }


    void menu(User currentUser) {
        int n = 1;
        while (n != 0) {
            while (true) {
                try {
                    System.out.print(
                            "\nMENU\n1.Show my profile\n2.Update profile\n3.Show my friends\n4.Add friends\n5.Remove friends\n6.Add notes\n7.Show notes of my firends\n8.Show Events\n0.Exit\nEnter your choice: "
                    );
                    n = sc.nextInt();
                    if (n >= 0 && n <= 8)
                        break;
                } catch (InputMismatchException e) {
                    System.out.println("Enter valid option.");
                    sc.nextLine();
                }
            }

            switch (n) {
                case 1:
                    System.out.println("\n--- My Profile ---");
                    System.out.println("ID : " + this.id);
                    System.out.println("Name : " + this.name);
                    System.out.println("Email : " + this.email);
                    System.out.println("Date of Birth: " + this.dob);
                    System.out.println("College : " + this.college);
                    System.out.println("Bio : " + this.bio);
                    System.out.println("Interests : " + this.interests);
                    System.out.println("Professional : " + this.profSection);
                    break;

                case 2:
                    updateProfile();
                    break;

                case 3:
                    FileHandler.showFriends(currentUser.id);
                    break;

                case 4:
                    FileHandler.addFriends(currentUser.id);
                    break;

                case 5:
                    FileHandler.removeFriend(currentUser.id);
                    break;
                case 6:
                    currentUser.updateNote(currentUser.id);
                    break;

                case 7:
                    User.displayFriendsNotes(currentUser.id);
                    break;

                case 8:
                    int choice;

                    while (true) {
                        try {
                            System.out.println("\n--- User Event Display ---");
                            System.out.println("1. By Date");
                            System.out.println("2. By Interval");
                            System.out.println("3. By Keyword");
                            System.out.print("Enter your choice: ");
                            choice = sc.nextInt();
                            sc.nextLine(); // clear newline after integer input

                            if (choice < 1 || choice > 3) {
                                System.out.println("Invalid option. Please choose 1-3.");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a number (1-3).");
                            sc.nextLine(); // clear invalid input
                        }
                    }

                    switch (choice) {
                        case 1 -> {
                            LocalDate date = null;
                            while (true) {
                                System.out.print("Enter date (yyyy-mm-dd): ");
                                String input = sc.nextLine().trim();
                                try {
                                    date = LocalDate.parse(input);
                                    break;
                                } catch (DateTimeParseException ex) {
                                    System.out.println("Invalid date format. Please use yyyy-mm-dd.");
                                }
                            }
                            eventDisplay.showEventsByDate(date, this.college);
                        }

                        case 2 -> {
                            LocalDate start = null, end = null;
                            while (true) {
                                try {
                                    System.out.print("Enter start date (yyyy-mm-dd): ");
                                    String startStr = sc.nextLine().trim();
                                    start = LocalDate.parse(startStr);

                                    System.out.print("Enter end date (yyyy-mm-dd): ");
                                    String endStr = sc.nextLine().trim();
                                    end = LocalDate.parse(endStr);

                                    if (end.isBefore(start)) {
                                        System.out.println("End date cannot be before start date. Try again.");
                                        continue;
                                    }
                                    break;
                                } catch (DateTimeParseException ex) {
                                    System.out.println("Invalid date format. Please use yyyy-mm-dd.");
                                }
                            }
                            eventDisplay.showEventsByInterval(start, end, this.college);
                        }

                        case 3 -> {
                            eventDisplay.showPersonalizedEvents(this);
                        }

                        default -> System.out.println("Invalid choice.");
                    }
                    break;
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    static void displayFriendsNotes(int currentUserId) {
        FileHandler.loadUserNotes();
        ArrayList<Integer> friendIds = FileHandler.friendsMap.get(currentUserId);

        if (friendIds == null || friendIds.isEmpty()) {
            System.out.println("You have no friends yet to view notes.");
            return;
        }

        System.out.println("\n--- Friends' Notes ---");
        boolean foundNotes = false;

        for (int friendId : friendIds) {
            String note = FileHandler.userNotesMap.get(friendId);
            User friend = FileHandler.usersMap.get(friendId);

            if (note != null && !note.trim().isEmpty() && friend != null) {
                System.out.println("===================================");
                System.out.println("Friend: " + friend.name);
                System.out.println("Note : " + note);
                System.out.println("===================================\n");
                foundNotes = true;
            }
        }

        if (!foundNotes) {
            System.out.println("No friends have set a visible note.");
        }
    }

    // method to write or update notes
    void updateNote(int userId) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your new note: ");
        currentNote = sc.nextLine();

        if (currentNote.length() > 250) {
            System.out.println("Note too long. Please keep it under 250 characters.");
            return;
        }
// 1. Update the in map
        FileHandler.userNotesMap.put(userId, currentNote);

// 2. Save to file
        FileHandler.saveUserNotes();
        System.out.println("Note updated successfully!");
    }
}

class FileHandler {

    static HashMap<Integer, User> usersMap = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> friendsMap = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> pendingrequestsMap = new HashMap<>(); //saves the connection requests
    static HashMap<Integer, Event> eventsMap = new HashMap<>();
    static HashMap<Integer, String> userNotesMap = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    public void signup() {
        User u = new User();
        u.signup();
        u.id = assignId("profile.txt");
        addToFile(u);
    }

    static int assignId(String filename) {
        int maxId = 99;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split("\\|");
                if (parts.length > 0) {
                    maxId = Integer.parseInt(parts[0]);
                }
            }
        } catch (IOException e) {
// file may not exist yet, start from 100
        }
        return maxId + 1;
    }

    static void addToFile(User u) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < u.interests.size(); i++) {
            sb.append(u.interests.get(i));
            if (i < u.interests.size() - 1)
                sb.append(";");
        }
        String interestsStr = sb.toString();

        try (BufferedWriter obj = new BufferedWriter(new FileWriter("profile.txt", true))) {
            obj.write(u.id + "|" + u.email + "|" + u.password + "|" + u.name + "|" + u.dob + "|" + u.college + "|"
                    + u.bio + "|"
                    + interestsStr + "|" + u.profSection);
            obj.newLine();
            obj.close();
        } catch (IOException e) {
            System.out.println("There is an error");
        }
// friends file
        try {
            FileWriter obj = new FileWriter("friends.txt", true);
            obj.write(u.id + ": \n");
            obj.close();
        } catch (IOException e) {
            System.out.println("There is an error");
        }
    }

    static void loadProfiles() { // loads info from profile into hmap
        try (BufferedReader br = new BufferedReader(new FileReader("profile.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                User u = new User();
                if (line.trim().isEmpty())
                    continue;

                String[] parts = line.split("\\|");
                if (parts.length < 9)
                    continue;

                u.id = Integer.parseInt(parts[0]);
                u.email = parts[1];
                u.password = parts[2];
                u.name = parts[3];
                u.dob = parts[4];
                u.college = parts[5];
                u.bio = parts[6];

                u.interests = new ArrayList<>();
                String[] nums = parts[7].split(";");
                for (String n : nums) {
                    u.interests.add(Integer.parseInt(n.trim()));
                }
                u.profSection = parts[8];

                usersMap.put(u.id, u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadFriends() { // loads data in friends hmap
        File friendsFile = new File("friends.txt");
        if (!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        friendsMap.clear(); //Clear old map before loading to avoid duplicates
// loads data in friendsMap hash map
        try (BufferedReader br = new BufferedReader(new FileReader(friendsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(":");
                if (parts.length < 2) continue; //invalid format

                int userId;
                try {
                    userId = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    continue; // skip bad line
                }

                ArrayList<Integer> friends = new ArrayList<>();

// Split friend IDs safely and skip blanks
                String[] friendIds = parts[1].split(",");
                for (String f : friendIds) {
                    f = f.trim();
                    if (!f.isEmpty()) { // avoids "" after trailing comma
                        try {
                            friends.add(Integer.parseInt(f));
                        } catch (NumberFormatException e) {
// ignore invalid number
                        }
                    }
                }

                friendsMap.put(userId, friends);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveProfiles() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("profile.txt"))) {
            for (User u : usersMap.values()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < u.interests.size(); i++) {
                    sb.append(u.interests.get(i));
                    if (i < u.interests.size() - 1)
                        sb.append(";");
                }
                String interestsStr = sb.toString();

                bw.write(u.id + "|" + u.email + "|" + u.password + "|" + u.name + "|" + u.dob + "|" +
                        u.college + "|" + u.bio + "|" + interestsStr + "|" + u.profSection);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveFriends() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("friends.txt"))) {
            for (Map.Entry<Integer, ArrayList<Integer>> entry : friendsMap.entrySet()) {
                int userId = entry.getKey();
                ArrayList<Integer> friends = entry.getValue();

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < friends.size(); i++) {
                    sb.append(friends.get(i));
                    if (i < friends.size() - 1)
                        sb.append(";");
                }
                writer.write(userId + ":" + sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    static void saveUserNotes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("notes.txt"))) {
            for (Map.Entry<Integer, String> entry : userNotesMap.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static void loadUserNotes() {
        try (BufferedReader reader = new BufferedReader(new FileReader("notes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split("\\|", 2); // Split only on the first '|'
                if (parts.length == 2) {
                    userNotesMap.put(Integer.parseInt(parts[0]), parts[1]);
                }
            }
        } catch (IOException ex) {
            System.out.println("No existing notes file found.");
        }
    }

    static void save_and_load_requests(String purpose) //saves the connection requests to the file
    {
        if (purpose.equalsIgnoreCase("save")) {
            try (BufferedWriter obj = new BufferedWriter(new FileWriter("requests.txt"))) {
                for (Map.Entry<Integer, ArrayList<Integer>> entry : pendingrequestsMap.entrySet()) {
                    int receiverID = entry.getKey();
                    ArrayList<Integer> sendersList = entry.getValue();
                    if (sendersList.isEmpty())
                        continue;
                    obj.write(receiverID + ":");
                    for (int i = 0; i < sendersList.size(); i++) {
                        obj.write(String.valueOf(sendersList.get(i)));
                        if (i < sendersList.size() - 1) {
                            obj.write(";");
                        }
                    }
                    obj.newLine();
                }
            } catch (IOException e) {
                System.out.println("There is an error");
            }
        } else if (purpose.equalsIgnoreCase("load")) //loads data from requests.txt file into pendingrequestsMap hashmap
        {
            pendingrequestsMap.clear(); //remove duplicates
            try (BufferedReader obj = new BufferedReader(new FileReader("requests.txt"))) {
                String line;
                while ((line = obj.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        int receiverID = Integer.parseInt(parts[0].trim());
                        String[] senders = parts[1].split(";");
                        ArrayList<Integer> sendersList = new ArrayList<>();
                        for (String sender : senders) {
                            if (!sender.trim().isEmpty()) {
                                sendersList.add(Integer.parseInt(sender.trim()));
                            }
                        }
                        pendingrequestsMap.put(receiverID, sendersList);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("There is an error");
            }
        }
    }

    static HashSet<Integer> getMutualFriends(int userID1, int userID2) {
//userID1 is ID of current user and userID2 is the ID of one of the users i.e. the candidate whose mutuals are to be calculated
        HashSet<Integer> setA = new HashSet<>(friendsMap.getOrDefault(userID1, new ArrayList<>()));
//set A: To get the friends of the current user
        HashSet<Integer> setB = new HashSet<>(friendsMap.getOrDefault(userID2, new ArrayList<>()));
//set B: To get the friends of the candidate
        setA.retainAll(setB);
//retainAll: intersection of two sets: will retain those which are common for both lists i.e. the friends common to both current user and the candidate are stored
        return setA;
    }

    static ArrayList<Recommendation> generateResults(int userID, String purpose, String nameFilter, HashSet<Integer> interestFilter) {
//generateResults() is a helper method which is useful for recommendation as well as search because both have same logic.
//Hence instead of repeating same logic for 2 different methods of recommendation and search, a single helper method is made containing the core logic and can be called as per requirement
// String purpose=> to search by name,interests or recommend people based on mutuals,common interests.
        ArrayList<Recommendation> result = new ArrayList<>();
        MaxHeap heap = new MaxHeap();
        HashSet<Integer> currentFriendSet = new HashSet<>(friendsMap.getOrDefault(userID, new ArrayList<>()));
        HashSet<Integer> resultSet = new HashSet<>();
        for (Map.Entry<Integer, User> entry : usersMap.entrySet()) {

            int candidateID = entry.getKey(); //candidateID is the user id of any user named as candidate. Used for checking whether he can be a potential friend of the user
            User candidate = entry.getValue();
            if (candidateID == userID || currentFriendSet.contains(candidateID)) {
                continue;
//we cannot recommend the user himself and the people who are already friends of the user
            }
//---------------Filter for search by name---------------
            if (purpose.equalsIgnoreCase("search_by_name")) {
                if (nameFilter == null || !candidate.name.toLowerCase().contains(nameFilter.toLowerCase().trim())) {
                    continue;
                }
//---------------calculate mutual friends---------------:
                resultSet = getMutualFriends(userID, candidateID);
            } else if (purpose.equalsIgnoreCase("recommendations_based_on_mutuals")) {
//---------------calculate mutual friends---------------:
                resultSet = getMutualFriends(userID, candidateID);
                if (resultSet.isEmpty()) {
                    continue; //skip with 0 mutual friends
                }
            }
//---------------calculate matching interests---------------:
//HashSet<Integer> matchedInterests;
            if (purpose.equalsIgnoreCase("search_by_interest")) {
//Filter for search by interests
                if (interestFilter.isEmpty()) {
                    continue;
                }
                resultSet = new HashSet<>(candidate.interests);
                resultSet.retainAll(interestFilter);
            } else if (purpose.equalsIgnoreCase("recommendations_based_on_interests")) {
                resultSet = new HashSet<>(usersMap.get(userID).interests);
                resultSet.retainAll(candidate.interests);
                if (resultSet.isEmpty()) {
                    continue; //skip with 0 common interests
                }
            }
            heap.insert(new Recommendation(candidateID, resultSet.size(), resultSet));
        }
        while (!heap.isEmpty() && result.size() < 5) {
            result.add(heap.extractMax());
        }
        return result;
    }

    static void showRecommendations(int userID) {
        System.out.println("Which type of recommendations would you like to see:\n1.Recommendations based on mutual friends.\n2.Recommendations based on common interests.");
        int choice1 = sc.nextInt();
        ArrayList<Recommendation> result = new ArrayList<>();
        switch (choice1) {
            case 1:
                result = generateResults(userID, "recommendations_based_on_mutuals", null, null);
                if (result.isEmpty()) {
                    System.out.println("No recommendations based on mutuals today:");
                    return;
                }
                System.out.println("Connect with people who are also connected with your friends");
                System.out.println("====================================================");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("User ID: " + result.get(i).candidateId + "| Name: " + usersMap.get(result.get(i).candidateId).name + " ");
                    System.out.println(".\n" + result.get(i).score + " of your friends are connected to this user");
                    System.out.println("Names of mutuals:\n");
                    for (int j : result.get(i).recommendations) {
                        System.out.println("ID: " + j + "| Name: " + usersMap.get(j).name);
                    }
                    System.out.println("====================================================");
                }
                break;
            case 2:
                result = generateResults(userID, "recommendations_based_on_interests", null, null);
                if (result.isEmpty()) {
                    System.out.println("No recommendations based on common interests today:");
                    return;
                }
                System.out.println("Connect with people having common interests.");
                System.out.println("====================================================");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("User ID: " + result.get(i).candidateId + "| Name: " + usersMap.get(result.get(i).candidateId).name + "\n You and " + usersMap.get(result.get(i).candidateId).name + " have " + result.get(i).score + " common interests");
                    System.out.println("Out of \n1.AI 2. Web Development 3. App Development 4. Robotics 5. Music 6. Photography 7.Gaming 8. Reading 9. Sports 10. ML\nYou have following Common interests:");
                    System.out.println(result.get(i).recommendations);
                    System.out.println("====================================================");
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    static void search_Friends(int userID) {
        ArrayList<Recommendation> result = new ArrayList<>();
        String ch;
        System.out.println("\n\n1.Search by name.\n2.Search by interests");
        int choice1;
        while (true) {
            try {
                choice1 = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Enter a positive integer as a choice");
                sc.nextLine();
            }
        }
        switch (choice1) {
            case 1:
                System.out.println("Enter the name of the user:");
                sc.nextLine();
                String search_name = sc.nextLine();
                result = generateResults(userID, "search_by_name", search_name, null);
                System.out.println("====================================================");
                if (result.isEmpty()) {
                    System.out.println("User not found");
                    System.out.println("====================================================");
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("User ID: " + result.get(i).candidateId + "| Name: " + usersMap.get(result.get(i).candidateId).name + ".\n" + result.get(i).score + " of your friends are connected to this user");
                    System.out.println("Names of mutuals:\n");
                    for (int j : result.get(i).recommendations) {
                        System.out.println("ID: " + j + "| Name: " + usersMap.get(j).name);
                    }
                    System.out.println("====================================================");
                }
                break;
            case 2:
                System.out.println("Input the number of interests:");
                int n = sc.nextInt();
                System.out.println("Out of \n1.AI 2. Web Development 3. App Development 4. Robotics 5. Music 6. Photography 7.Gaming 8. Reading 9. Sports 10. ML\nEnter the serial numbers of " + n + " interests");
                HashSet<Integer> search_interests = new HashSet<>();
                int interest;
                for (int i = 0; i < n; i++) {
                    while (true) {
                        try {
                            interest = sc.nextInt();
                            if (interest < 0 || interest > 10) {
                                throw new InvalidInformationException("Enter the serial numbers in the range of 1 to 10");
                            }
                            search_interests.add(interest);
                            break;
                        } catch (InvalidInformationException e) {
                            System.out.println(e.getMessage());
                        } catch (InputMismatchException e) {
                            System.out.println("Enter the integer serial numbers of the interests shown above");
                            sc.nextLine();
                        }
                    }
                }
                result = generateResults(userID, "search_by_interest", null, search_interests);
                System.out.println("====================================================");
                if (result.isEmpty()) {
                    System.out.println("Users with given interests not found");
                    System.out.println("====================================================");
                    return;
                }
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("User ID: " + result.get(i).candidateId + "| Name: " + usersMap.get(result.get(i).candidateId).name + "\n You and " + usersMap.get(result.get(i).candidateId).name + " have " + result.get(i).score + " matching interests");
                    System.out.println("Interests are matching with the input set given by you:\n" + result.get(i).recommendations);
                    HashSet<Integer> mutuals = getMutualFriends(userID, result.get(i).candidateId);
                    if (mutuals.size() != 0) {
                        System.out.println("The user has " + mutuals.size() + " mutual friends.\nThe names of mutual friends are:");
                        for (int j : mutuals) {
                            System.out.println(usersMap.get(j).name);
                        }
                        System.out.println("====================================================");
                    }
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    static void sendConnectionRequest(int userID) {
        System.out.print("Enter the user ID of the user you want to connect with: ");
        int receiverID;
        while (true) {
            try {
                receiverID = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Enter the ID as a positive integer");
                sc.nextLine();
            }
        }
        if (!usersMap.containsKey(receiverID)) {
            System.out.println("Invalid ID");
            return;
        }
        if (receiverID == userID) {
            System.out.println("You cannot send a connection request to yourself");
            return;
        }
        if (friendsMap.getOrDefault(userID, new ArrayList<>()).contains(receiverID)) {
            System.out.println("You are already connected");
            return;
        }
        ArrayList<Integer> requestSenders = pendingrequestsMap.getOrDefault(receiverID, new ArrayList<>()); //list of the users who send requests
        if (requestSenders.contains(userID)) {
            System.out.println("You have already connection request to " + receiverID);
            return;
        }
        requestSenders.add(userID);
        pendingrequestsMap.put(receiverID, requestSenders);
        save_and_load_requests("save");
        System.out.println("Connection request sent to " + receiverID);
    }

    static void view_Manage_Requests(int userID) {
        save_and_load_requests("load");
        ArrayList<Integer> received = pendingrequestsMap.getOrDefault(userID, new ArrayList<>());
        System.out.println("Menu:\n1.View received requests.\n2.View sent requests");
        int choice1 = sc.nextInt();
        switch (choice1) {
            case 1: //view received requests
                if (received.isEmpty()) {
                    System.out.println("No recent requests");
                } else {
                    System.out.println("You have received requests from:\n");
                    System.out.println("====================================================");
                    Iterator<Integer> itr = received.iterator();
                    while (itr.hasNext()) {
                        int sender = itr.next(); //sender represents the user ID of each request sender
                        System.out.println("User ID: " + sender + "| Name: " + usersMap.get(sender).name + "\nCollege: " + usersMap.get(sender).college + "\nInterests: " + usersMap.get(sender).interests + "\nBio: " + usersMap.get(sender).bio);
                        if (!usersMap.get(sender).profSection.equalsIgnoreCase("NA")) {
                            System.out.println("| Professional status: " + usersMap.get(sender).profSection);
                        }
                        System.out.println("\nTake some action:\n1. Accept a request");
                        System.out.println("2. Decline a request");
                        System.out.println("3. Do nothing and return");
                        System.out.print("Enter your choice: ");
                        int subChoice = sc.nextInt();
                        if (subChoice == 1) {
//for accepted requests add the users in the friends lists of each other
                            itr.remove(); //safe removal using iterator
                            pendingrequestsMap.put(userID, received); //to remove sender from the pending requests as now he is a friend of user
                            friendsMap.computeIfAbsent(userID, k -> new ArrayList<>()).add(sender);
                            friendsMap.computeIfAbsent(sender, k -> new ArrayList<>()).add(userID);
                            saveFriends();
                            System.out.println("\n----------------You are now friends with " + usersMap.get(sender).name + "----------------");
                        } else if (subChoice == 2) {
//for declined requests remove the request from the file and the hashmap and no addition to the friends' lists of the sender and receiver as request is declined
                            itr.remove(); //safe removal using iterator
                            pendingrequestsMap.put(userID, received); //to remove sender from the pending requests as his request is declined by the user
                            System.out.println("You declined the connection request from " + usersMap.get(sender).name);
                        } else {
                            continue;
                        }
                        System.out.println("====================================================");
                        System.out.println();
                    }
                    save_and_load_requests("save"); //save changes in map after declining or accepting requests
                }
                break;
            case 2: //view sent requests:
                Boolean hasSent = false;
                for (Map.Entry<Integer, ArrayList<Integer>> entry : pendingrequestsMap.entrySet()) {
                    System.out.println("====================================================");
                    if (entry.getValue().contains(userID)) {
                        int recipientID = entry.getKey();
                        System.out.println("You had sent connection requests to:\n\nUser ID: " + recipientID + "| Name: " + usersMap.get(recipientID).name + "\n|College: " + usersMap.get(recipientID).college + "| Interests: " + usersMap.get(recipientID).interests);
                        if (!usersMap.get(recipientID).profSection.equalsIgnoreCase("NA")) {
                            System.out.println("Professional status: " + usersMap.get(recipientID).profSection);
                        }
                        System.out.println("\nDo you want to withdraw request? Enter 'yes' or 'no'");
                        if (sc.next().equalsIgnoreCase("yes")) {
//for declined requests remove the request from the file and the hashmap
                            ArrayList<Integer> sentList = pendingrequestsMap.getOrDefault(recipientID, new ArrayList<>());
                            sentList.remove(Integer.valueOf(userID));
                            pendingrequestsMap.put(recipientID, sentList);
                            System.out.println("You have withdrawn your connection request you had sent to " + usersMap.get(recipientID).name);
                            System.out.println("====================================================");
                        }
                    }
                    hasSent = true;
                }
                if (hasSent != true) {
                    System.out.println("You had sent no requests to anyone recently");
                    break;
                }
                save_and_load_requests("save"); //save changes in map if the user might have withdrawn the sent request
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    static void addFriends(int userID) // userID: ID of current (logged in) user
    {
        User currUser = usersMap.get(userID);
        if (currUser == null) {
            System.out.println("The user does not exist");
            return;
        }
        String ch;
        do {
            System.out.println("=============ADD FRIENDS=============\nMenu:\n1.See recommendations.\n2.Search people.\n3.Send connection request.\n4.View and manage requests");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    showRecommendations(userID);
                    break;
                case 2:
                    search_Friends(userID);
                    break;
                case 3:
                    sendConnectionRequest(userID);
                    break;
                case 4:
                    view_Manage_Requests(userID);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            sc.nextLine();
            System.out.println("\nDo you want to stay on Add friends page and view more.\nEnter 'yes' or 'no' ");
            while (true) {
                try {
                    ch = sc.nextLine();
                    if (!ch.equalsIgnoreCase("yes") && !ch.equalsIgnoreCase("no")) {
                        throw new InvalidInformationException("Either enter 'yes' or 'no'");
                    }
                    break;
                } catch (InvalidInformationException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (ch.equalsIgnoreCase("yes"));
    }

    static void showFriends(int userID) //userID:ID of current (logged in) user
    {
        ArrayList<Integer> userFriends = friendsMap.getOrDefault(userID, new ArrayList<>());
// the list of friends of the user
        if (userFriends.isEmpty()) {
            System.out.println("You have no friends yet");
            return;
        }
        System.out.println("You have " + userFriends.size() + " connections");
        System.out.println("\n-------------------------------YOUR FRIENDS-------------------------------");
        for (int friendID : userFriends) {
            User friend = usersMap.get(friendID);
            System.out.println("User ID: " + friend.id + " | Name: " + friend.name);
            System.out.println("Do you want to view your friend's information?Enter 'yes' or 'no'");
            String choice = sc.next();
            if (choice.equalsIgnoreCase("yes")) {
                System.out.println("User ID: " + friend.id + " | Name: " + friend.name + "\nCollege: " + friend.college + "| Interests: " + friend.interests + "\nBio: " + friend.bio);
                if (!friend.profSection.equalsIgnoreCase("NA")) {
                    System.out.println("Professional Status: " + friend.profSection);
                }
            }
        }
    }

    static void removeFriend(int userID) // userID: ID of current (logged in) user
    {
        ArrayList<Integer> userFriends = friendsMap.getOrDefault(userID, new ArrayList<>());
// the list of friends of the user
        if (userFriends.isEmpty()) {
            System.out.println("No friends in the list");
            return;
        }
        showFriends(userID);
        System.out.print("Enter the user ID of the friend you want to remove: ");
        int removeID = sc.nextInt();
        if (!userFriends.contains(removeID)) {
            System.out.println("This user is not in your friends list");
            return;
        }
//confirmation
        System.out.println("Are you sure that you want to remove the friend with user ID " + removeID + " and name " + usersMap.get(removeID).name + "?\nEnter yes or no");
        if (sc.next().equalsIgnoreCase("no")) {
            System.out.println("No removal of friend with user ID" + removeID + " and name " + usersMap.get(removeID).name + "\nNo changes made in your friends list");
            return;
        }
//Remove the friendID from the arraylist
        userFriends.remove(Integer.valueOf(removeID));
//update the friendsMap by assigning the userFriends with removed friendID to the value of the hashmap
        friendsMap.put(userID, userFriends);
//Remove from the removed friend's friends list also
        ArrayList<Integer> otherList = friendsMap.getOrDefault(removeID, new ArrayList<>());
        otherList.remove(Integer.valueOf(userID));
        friendsMap.put(removeID, otherList);
        System.out.println("You are no longer friends with " + usersMap.get(removeID).name);
    }

    static void loadEvents() {
        eventsMap.clear(); // clear previous data first
        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|", -1); // keep empty fields
                if (parts.length < 11) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    Event e = new Event();
                    e.eventId = Integer.parseInt(parts[0]);
                    e.status = Integer.parseInt(parts[1]);
                    e.eventName = parts[2];
                    e.day = parts[3];
                    e.timings = parts[4];
                    e.collegeName = parts[5];
                    e.description = parts[6];
                    e.location = parts[7];
                    e.eligibility = parts[8];
                    e.date = LocalDate.parse(parts[9]);
                    e.keywords = new ArrayList<>();

                    if (!parts[10].isEmpty()) {
                        e.keywords.addAll(Arrays.asList(parts[10].split(";")));
                    }

                    eventsMap.put(e.eventId, e);
                } catch (Exception ex) {
                    System.out.println("Error parsing line: " + line);
                }
            }
        } catch (IOException ex) {
            System.out.println("No existing file found. It will be created later.");
        }
    }

    static void saveEvents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt"))) {
            for (Event e : eventsMap.values()) {
                String keywordsStr = String.join(";", e.keywords);
                writer.write(e.eventId + "|" + e.status + "|" + e.eventName + "|" + e.day + "|" +
                        e.timings + "|" + e.collegeName + "|" + e.description + "|" +
                        e.location + "|" + e.eligibility + "|" + e.date + "|" + keywordsStr);
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}


class Event {

    int eventId;
    String eventName;
    String day;
    LocalDate date;
    String timings;
    String collegeName;
    String description;
    String location; // venue
    String eligibility;
    ArrayList<String> keywords = new ArrayList<>();
    int status; // 1 = active, 0 = completed/deleted
}

class eventDisplay {
    static final String[] interestNames = {
            "", "AI", "Web Development", "App Development", "Robotics",

            "Music", "Photography", "Gaming", "Reading", "Sports", "ML"
    };

    //Show events by date for user's college
    static void showEventsByDate(LocalDate date, String userCollege) {

        FileHandler.loadEvents();
        boolean found = false;

        for (Event e : FileHandler.eventsMap.values()) {

            if (e.status == 1 && e.date.equals(date) &&

                    e.collegeName.equalsIgnoreCase(userCollege)) {

                printEvent(e);
                found = true;
            }
        }
        if (!found)
            System.out.println(" No events found for your college on this date.");
    }
//Show events by interval for user's college

    static void showEventsByInterval(LocalDate start, LocalDate end, String userCollege) {

        FileHandler.loadEvents();
        boolean found = false;
        for (Event e : FileHandler.eventsMap.values()) {
            if (e.status == 1 && !e.date.isBefore(start) && !e.date.isAfter(end) && e.collegeName.equalsIgnoreCase(userCollege)) {
                printEvent(e);
                found = true;
            }
        }
        if (!found)
            System.out.println(" No events found for your college in this range.");
    }

    static void showPersonalizedEvents(User user) {

        FileHandler.loadEvents();
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEvents Based on Your Interests ---");
        showByInterests(user);
        System.out.print("\nDo you want to search using extra keywords too? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (ans.equals("y")) {
            showByKeywords(user);
        }
    }

    //EVENTS BASED ON USER INTEREST
    static void showByInterests(User user) {
        HashMap<Event, Integer> matchCount = new HashMap<>();
        for (Event e : FileHandler.eventsMap.values()) {
            if (e.status == 1 && e.collegeName.equalsIgnoreCase(user.college)) {
                int count = 0;
                for (int interestId : user.interests) {
                    if (interestId > 0 && interestId < interestNames.length) {
                        String interestName = interestNames[interestId].toLowerCase();
                        for (String kw : e.keywords) {
                            if (kw.toLowerCase().contains(interestName)) {
                                count++;
                            }
                        }
                    }
                }
                if (count > 0)
                    matchCount.put(e, count);
            }
        }
        displaySortedEvents(matchCount, "Interest Match");
    }

//EVENTS BASED ON ADDITIONAL KEYWORDS

    static void showByKeywords(User user) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter extra keywords (space-separated): ");
        String[] input = sc.nextLine().trim().split("\\s+");
        ArrayList<String> extraKeywords = new ArrayList<>();
        for (String kw : input) {
            if (!kw.isEmpty())
                extraKeywords.add(kw.toLowerCase());
        }
        HashMap<Event, Integer> matchCount = new HashMap<>();
        for (Event e : FileHandler.eventsMap.values()) {
            if (e.status == 1 && e.collegeName.equalsIgnoreCase(user.college)) {
                int count = 0;
                for (String extra : extraKeywords) {
                    for (String kw : e.keywords) {
                        if (kw.toLowerCase().contains(extra))
                            count++;
                    }
                }
                if (count > 0)
                    matchCount.put(e, count);
            }
        }
        displaySortedEvents(matchCount, "Keyword Match");
    }

//COMMON DISPLAY FUNCTION — SORTED BY MATCH COUNT (DECREASING)

    static void displaySortedEvents(HashMap<Event, Integer> matchCount, String matchType) {
        List<Map.Entry<Event, Integer>> sortedList = new ArrayList<>(matchCount.entrySet());
//Sort by decreasing number of matches
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        if (sortedList.isEmpty()) {
            if (matchType.equals("Interest Match"))
                System.out.println("No matching events found based on your interests.");
            else
                System.out.println("No matching events found for entered keywords.");
            return;
        }
        System.out.println("\n --- Events Sorted by " + matchType + " Count ---");
        for (Map.Entry<Event, Integer> entry : sortedList) {
            Event e = entry.getKey();
            int matches = entry.getValue();
            printEvent(e, matchType + ": " + matches);
        }
    }

    static void printEvent(Event e, String matchInfo) {

        System.out.println("--------------------------------------------------");
        System.out.println("Event Name : " + e.eventName + " (" + matchInfo + ")");
        System.out.println("Event ID : " + e.eventId);
        System.out.println("Date : " + e.date + " (" + e.day + ")");
        System.out.println("Time : " + e.timings);
        System.out.println("College : " + e.collegeName);
        System.out.println("Location : " + e.location);
        System.out.println("Eligibility : " + e.eligibility);
        System.out.println("Description : " + e.description);
        System.out.println("Keywords : " + String.join(", ", e.keywords));
        System.out.println("--------------------------------------------------\n");
    }

    //Nice formatted printing of an event
    static void printEvent(Event e) {
        System.out.println("===================================");
        System.out.println("Event ID : " + e.eventId);
        System.out.println("Name : " + e.eventName);
        System.out.println("Date : " + e.date + " (" + e.day + ")");
        System.out.println("Time : " + e.timings);
        System.out.println("College : " + e.collegeName);
        System.out.println("Location : " + e.location);
        if (e.status == 1) {
            System.out.println("Status: Yet to Begin");
        } else {
            System.out.println("Status: Completed");
        }
        System.out.println("Eligibility: " + e.eligibility);
        System.out.println("Description: " + e.description);
        System.out.println("Keywords : " + String.join(", ", e.keywords));
        System.out.println("===================================\n");
    }
}

class Admin {

    static Scanner sc = new Scanner(System.in);

    static void login() {
        final String ADMIN_ID = "admin123";
        final String ADMIN_PASSWORD = "pass123";
        int attempts = 3;
        System.out.println("\n--- Admin Login ---");
        while (attempts > 0) {
            System.out.print("Enter Admin ID: ");
            String id = sc.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = sc.nextLine().trim();
            if (id.equals(ADMIN_ID) && password.equals(ADMIN_PASSWORD)) {
                System.out.println("Login successful!\n");
//Proceed to admin menu
                menu();
                return;
            } else {
                attempts--;
                if (attempts > 0)
                    System.out.println("Invalid credentials. Attempts left: " + attempts + "\n");
                else
                    System.out.println("Too many failed attempts. Access denied.\n");
            }
        }
    }

    static String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This field cannot be empty.");
            }
        } while (input.isEmpty());
        return input;
    }

    static void menu() {
        int choice = -1;
        do {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Event");
            System.out.println("2. Edit Event");
            System.out.println("3. Delete Event (mark completed)");
            System.out.println("4. Show All Events");
            System.out.println("5. Logout");
            while (true) {
                System.out.print("Enter choice (1-5): ");
                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                    if (choice < 1 || choice > 5) {
                        System.out.println("Please enter a valid option between 1 and 5.");
                    } else {
                        break; // valid choice
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number between 1 and 5.");
                    sc.nextLine(); // clear invalid input
                }
            }
            switch (choice) {
                case 1 -> addEvent();
                case 2 -> editEvent();
                case 3 -> deleteEvent();
                case 4 -> displayAllEvents();
                case 5 -> System.out.println("Logging out...");
            }
        } while (choice != 5);
    }

    static void displayAllEvents() {
        FileHandler.loadEvents();
        if (FileHandler.eventsMap.isEmpty()) {
            System.out.println("No events available yet.");
            return;
        }
        System.out.println("\n--- All Events ---");
        for (Event e : FileHandler.eventsMap.values()) {
            eventDisplay.printEvent(e);
        }
    }

    static void editEvent() {
        FileHandler.loadEvents();
        int id;
        while (true) {
            try {
                System.out.print("Enter Event ID to edit: ");
                id = sc.nextInt();
                sc.nextLine();
                if (id > 0) break;
                else System.out.println("Please enter a valid positive Event ID.");
            } catch (InputMismatchException ex) {
                System.out.println("Invalid ID! Please enter a numeric Event ID.");
                sc.nextLine();
            }
        }
        Event e = FileHandler.eventsMap.get(id);
        if (e == null || e.status == 0) {
            System.out.println("Event not found or inactive.");
            return;
        }
        int choice = 0;
        do {
            System.out.println("\n--- Edit Menu ---");
            System.out.println("1. Name\n2. Date\n3. Description\n4. Location\n5. Day\n6.Eligibility\n7. Done");
            System.out.print("Enter your choice (1-5): ");
            try {
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1 -> e.eventName = getNonEmptyInput("Enter New Name: ");
                    case 2 -> {
                        while (true) {
                            System.out.print("New Date (yyyy-mm-dd): ");
                            try {
                                LocalDate d = LocalDate.parse(sc.nextLine().trim());
                                if (d.isBefore(LocalDate.now())) {
                                    System.out.println("Date cannot be in the past.");
                                } else {
                                    e.date = d;
                                    break;
                                }
                            } catch (DateTimeParseException ex) {
                                System.out.println("Invalid date format!");
                            }
                        }
                    }
                    case 3 -> e.description = getNonEmptyInput("Enter New Description: ");
                    case 4 -> e.location = getNonEmptyInput("Enter New Location: ");
                    case 5 -> e.day = getNonEmptyInput("Enter New Day: ");
                    case 6 -> e.eligibility = getNonEmptyInput("Enter New Eligibility: ");
                    case 7 -> System.out.println("Saving changes...");
                    default -> System.out.println("Invalid choice!");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input! Try again.");
                sc.nextLine();
            }
        } while (choice != 7);
        FileHandler.saveEvents();
        System.out.println("Event updated successfully!");
    }

    static void addEvent() {
        Event e = new Event();
        e.eventId = FileHandler.assignId("events.txt");
        e.status = 1;
        System.out.println("\n--- Add Event ---");
        e.eventName = getNonEmptyInput("Enter Event Name: ");
        e.timings = getNonEmptyInput("Enter Timings: ");
        int coll;
        while (true) {

            System.out.println("\nSelect College from one of the below");
            System.out.println("1.CCOEW");
            System.out.println("2.COEP");
            System.out.println("3.PICT");
            System.out.println("4.PVG");
            System.out.println("5.VIT");

            try {
                coll = sc.nextInt();
                if (coll > 0 && coll < 6)
                    break;

            } catch (InputMismatchException ex) {
                System.out.println("Enter valid option");
                sc.next();
            }

        }
        switch (coll) {
            case 1:
                e.collegeName = "CCOEW";
                break;

            case 2:
                e.collegeName = "COEP";
                break;

            case 3:
                e.collegeName = "PICT";
                break;

            case 4:
                e.collegeName = "PVG";
                break;

            case 5:
                e.collegeName = "VIT";
                break;

            default:
                System.out.println("Invalid input.");

        }
        e.description = getNonEmptyInput("Enter Description: ");
        e.location = getNonEmptyInput("Enter Location: ");
        e.eligibility = getNonEmptyInput("Enter Eligibility: ");
        e.day = getNonEmptyInput("Enter Day: ");
        while (true) {
            System.out.print("Enter Event Date (yyyy-mm-dd): ");
            String dateStr = sc.nextLine().trim();
            try {
                e.date = LocalDate.parse(dateStr);
                if (e.date.isBefore(LocalDate.now())) {
                    System.out.println("Event date cannot be in the past.");
                    continue;
                }
                break;
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            }
        }
        FileHandler.loadEvents();
        for (Event existing : FileHandler.eventsMap.values()) {
            if (existing.status == 1 &&
                    existing.eventName.equalsIgnoreCase(e.eventName) &&
                    existing.collegeName.equalsIgnoreCase(e.collegeName) &&
                    existing.date.equals(e.date)) {
                System.out.println("An event with the same name and date already exists for this college!");
                return; // stop the method, don’t add
            }
        }
//Taking keywords one by one
        e.keywords = new ArrayList<>();
        System.out.println("Enter keywords for the event one by one (type 'done' to finish):");
        while (true) {
            String kw = sc.nextLine().trim();
            if (kw.equalsIgnoreCase("done"))
                break;
            if (!kw.isEmpty())
                e.keywords.add(kw);
        }
        if (e.keywords.isEmpty()) {
            System.out.println("At least one keyword required. Please add some.");
            return;
        }
        FileHandler.eventsMap.put(e.eventId, e);
        FileHandler.saveEvents();
        System.out.println("Event added successfully!");
        System.out.println("The event's Id is: " + e.eventId);
    }

    static void deleteEvent() {
        FileHandler.loadEvents();
        System.out.print("Enter Event ID to mark completed: ");
        int id = sc.nextInt();
        sc.nextLine();
        Event e = FileHandler.eventsMap.get(id);
        if (e == null) {
            System.out.println("Event not found.");
            return;
        }
        if (e.status == 0) {
            System.out.println("This event is already marked as completed.");
            return;
        } else {
            System.out.println("The event is: ");
            eventDisplay.printEvent(e);
        }
        System.out.print("Are you sure you want to mark it as completed? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled.");
            return;
        }
        e.status = 0;
        FileHandler.saveEvents();
        System.out.println("Event marked as completed.");
    }
}


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User u = new User();
        FileHandler f = new FileHandler();
        int n;
        while (true) {
            System.out.println("\nMENU:\n1. Admin Menu\n2. User Menu\nEnter your choice:");
            try {
                n = sc.nextInt();
                if (n == 1 || n == 2) break;
                else {
                    System.out.println("Enter a valid option(1/2).");
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid option(1/2).");
                sc.next();
            }
        }

        sc.nextLine();

// User currentUser = new User(); // assume user is logged in
        switch (n) {
            case 1:
                Admin.login();
                break;
            case 2:
                int next;
                while (true) {
                    try {
                        System.out.println("\nMENU\n1.Signup\n2.Login");
                        next = sc.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Enter valid option.");
                        sc.next();
                    }
                }
                switch (next) {
                    case 1:
                        f.signup();
                        System.out.println("\nSignup successfull. Now refresh and login.");
                        break;
                    case 2:
                        FileHandler.loadProfiles();
                        FileHandler.loadFriends();
                        FileHandler.loadEvents();
                        User currentUser = new User();
                        while (true) {
                            currentUser = u.login();
                            if (currentUser != null) {
                                System.out.println("\nLogin Successfull.");
                                break;
                            } else {
                                System.out.println("\nInvalid email or password. Try again.");
                            }
                        }
                        currentUser.menu(currentUser);
                        break;

                    default:
                        System.out.println("Enter valid option.");
                }
            default:
                System.out.println("Enter valid option.");

        }
        sc.close();
    }
}

