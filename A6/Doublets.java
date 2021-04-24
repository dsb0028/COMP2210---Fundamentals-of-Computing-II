import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.lang.reflect.Array;

/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Daniel Benjamin (dsb0028@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2019-03-29
 */
public class Doublets implements WordLadderGame {

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   /////////////////////////////////////////////////////////////////////////////
   // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
   // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
   // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
   // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
   // table with chaining).
   /////////////////////////////////////////////////////////////////////////////
   TreeSet<String> lexicon;
   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         //////////////////////////////////////
         // INSTANTIATE lexicon OBJECT HERE  //
         //////////////////////////////////////
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            /////////////////////////////////////////////////////////////
            // INSERT CODE HERE TO APPROPRIATELY STORE str IN lexicon. //
            /////////////////////////////////////////////////////////////
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }


   //////////////////////////////////////////////////////////////
   // ADD IMPLEMENTATIONS FOR ALL WordLadderGame METHODS HERE  //
   //////////////////////////////////////////////////////////////
   /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
      int hammingDistance = 0;
      if(str1.length() == str2.length()) {
         for (int i = 0; i < str1.length(); i++) {
            if(str1.charAt(i) != str2.charAt(i)) {
               hammingDistance++;
            }
         }
         return hammingDistance;
      }
      return -1;
   }
   
      /**
   * Returns a minimum-length word ladder from start to end. If multiple
   * minimum-length word ladders exist, no guarantee is made regarding which
   * one is returned. If no word ladder exists, this method returns an empty
   * list.
   *
   * Breadth-first search must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a minimum length word ladder from start to end
   */
   
   public List<String> getMinLadder(String start, String end) {
      start = start.toLowerCase();
      end = end.toLowerCase();
      List<String> EMPTY_LADDER = new ArrayList<String>();
      ArrayList<String> backwards = new ArrayList<String>();
      List<String> minLadder = new ArrayList<String>();
      if (start.equals(end)) {
         minLadder.add(start);
         return minLadder;
      }
      
      if (getHammingDistance(start, end) == -1) {
         return EMPTY_LADDER;
      }
      
      if(isWord(start) && isWord(end)) {
         backwards = bfsMemoryForGetMinLadder(start, end);
      }
      
      if (backwards.isEmpty()) {
         return EMPTY_LADDER;
      }
      
      for (int i = backwards.size() - 1; i >= 0; i--) {
         minLadder.add(backwards.get(i));
      }
      return minLadder;
   }
   private class Node {
      String word;
      Node predecessor;
      public Node(String p, Node pred) {
         word = p;
         predecessor = pred;
      }
   }
   
       
   private ArrayList<String> bfsMemoryForGetMinLadder(String start, String end) {
      Deque<Node> queue = new ArrayDeque<Node>();
      TreeSet<String> visited = new TreeSet<String>();
      ArrayList<String> backwards = new ArrayList<String>();
      visited.add(start);
      Node firstNode = new Node(start, null);
      Node lastNode = new Node(end, null);
      Node nghbr = null;
      queue.addLast(firstNode);
      outerloop:
      while (!queue.isEmpty()) {
         Node n = queue.removeFirst();
         String wrd = n.word;
         for (String neighbor : getNeighbors(wrd)) {
            if(!visited.contains(neighbor)) {
               visited.add(neighbor);
               nghbr = new Node(neighbor, n);
               queue.addLast(nghbr);
               if(neighbor.equals(end)) {
                  lastNode.predecessor = n;
                  break outerloop;
               }
            }
         }
      }
      
      if(lastNode.predecessor == null)
      {
         return backwards;
      }
      Node l = lastNode;
      while (l != null) {
         backwards.add(l.word);
         l = l.predecessor;
      }
      return backwards;
   }
   
    
   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
      List<String> wordList = new ArrayList<String>();
      char [] charArray = word.toCharArray();
      char [] copyArray = Arrays.copyOf(charArray, charArray.length);
      String newWrd = "";
      for(int i = 0; i < charArray.length; i++) {
         for(char chr = 'a'; chr <= 'z'; chr++) {
            if (charArray[i] != chr) {
               Array.setChar(copyArray, i, chr);
               newWrd = new String(copyArray);
               if (isWord(newWrd)) {
                  wordList.add(newWrd);            
               }
            }
         }
         copyArray = Arrays.copyOf(charArray, charArray.length);
      
      }
      return wordList;
   }


   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() {
      return lexicon.size();
   }


   /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
   public boolean isWord(String str) {
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      if (str == null) {
         throw new IllegalArgumentException();
      }
      str = str.toLowerCase();
      return lexicon.contains(str);
   }

   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
   public boolean isWordLadder(List<String> sequence) {
      if(sequence.isEmpty()) {
         return false;
      }
      Iterator<String> itr = sequence.iterator();
      String first = itr.next();
      if(isWord(first)) {
         while(itr.hasNext()) {
            String word = itr.next();
            if(isWord(word)) {
               if(getHammingDistance(first, word) != 1) {
                  return false;
               }
            }
            
            else {
               return false;
            }
            first = word;
         }  
      }
      
      else {
         return false;
      }
      
      return true;
   }

}

