# college-event-social-network-system
A Java-based social networking system that connects college students through shared interests and event discovery.

# College Social Networking System

## Introduction
The College Social Networking System is designed to help students connect with peers who share similar interests and stay updated about events happening in their college. The platform focuses on interest-based connections, allowing students to build meaningful networks within their academic environment.

The system also includes an event feature where students can explore college events based on their interests or keywords, encouraging participation and community engagement.

---

## Problem Definition
In most colleges, students do not have a centralized platform to connect with peers who share similar interests or to stay informed about events within their institution.

Information about activities, clubs, and opportunities is often scattered across notice boards, chat groups, or social media platforms. This makes it difficult for students to discover relevant events or communities.

This project solves this problem by creating a system where students can build interest-based connections and discover college events curated according to their interests and keywords.

---

## Scope of the System
The system allows students to:

- Create and manage profiles  
- Add and remove friends  
- Search users by name or interests  
- View updates or notes from friends  
- Discover events based on interests, keywords, or date  

Admins can:

- Add events  
- Edit event details  
- Manage events within their own college  

Future improvements may include real-time chat and notifications.

---

## Data Structures Used

### HashMap
Used for storing user profiles, events, and friend lists.  
Provides fast access and retrieval with **O(1)** average time complexity.

### ArrayList
Used for storing dynamic collections such as interests, keywords, and friend lists.

### Arrays
Used for temporary storage when reading data from text files.

### HashSet
Used for storing unique elements like friend IDs and keywords and for finding intersections during recommendations.

### Custom Max Heap
Used for recommending users based on mutual friends or shared interests.

### Graph (Conceptual)
Friendships form a graph where each user is a node and each friendship is an edge.


---
## How to Run

1. Compile the program
javac osl2.java

2. Run the program
java osl2

Make sure all text files are present in the same folder.

---

## Technologies Used
- Java
- File Handling
- HashMap
- ArrayList
- HashSet
- Custom Heap

---

## Future Enhancements

- Chat system between users
- Real-time notifications for events
- Improved recommendation algorithms
- GUI or web interface

## Project Structure
