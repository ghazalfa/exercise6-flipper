package flips;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Flipper {

    /**
     * Checks whether or not a list of flips will bring src to dest.
     * Returns false if there is some swap that is an invalid flip.
     *
     * A swap is said to be a flip iff its swapping indices are adjacent
     *     i.e. they differ by one.
     * A swap (and by extension a flip) is said to be valid on a string s
     *     iff its swapping indices are within bounds for s.
     *
     * @param src -- the string to start from
     * @param dest -- the target string
     * @param flips -- a sequence of swaps to be performed on src
     * @return true iff flips is a sequence of valid flips that when applied
     *     to src results in dest
     */
    public static boolean flipsMatches(String src, String dest, List<Swap> flips) {
        int index1;
        int index2;
        char temp1;
        char temp2;
        char[] ch = src.toCharArray();

        for(Swap currentSwap: flips){

            index1 = currentSwap.getLeft();
            index2 = currentSwap.getRight();

            if(index1<0 || index2< 0){
                return false;
            }
            if(index1> ch.length||index2> ch.length){
                return false;
            }

            if(Math.abs(index1-index2)>1){
                return false;
            }

            temp1 = ch[index1];
            temp2 = ch[index2];

            ch[index1]= temp2;
            ch[index2]= temp1;
        }

        src = new String(ch);

        return src.equals(dest);
    }


    public static boolean isThereAValidWay(String src, String dest){
        char[] srcArray = src.toCharArray();
        char[] destArray = dest.toCharArray();
        boolean[] checked = new boolean[srcArray.length];
        boolean state;

        for(int i = 0; i<src.length(); i++){
            checked[i]=false;
        }

        if(srcArray.length != destArray.length){
            return false;
        }



        for(int i = 0; i<src.length(); i++){
            char temp = src.charAt(i);
            state = false;

            for(int m = 0; m<src.length();m++){
                if(temp==dest.charAt(m) && !checked[m] && !state){
                    checked[m] = true;
                    state = true;

                }
            }

            if(!state){
            return false;    }
        }

        return true;
    }

    public static int dedicatedToMatthewChow(char[] src, char temp, int traversed ){
        int index = 0;
        for(int i = src.length-traversed-1; i>=0; i--){
            if(src[i]==temp){
                return i;
            }
        }

        return index;
    }


    public static List<Swap> flipsSequenceRedone(String src, String dest){

        char[] srcArray = src.toCharArray();
        char[] destArray = dest.toCharArray();
        List<Swap> listOfSwap = new ArrayList<>();
        int count = 0;

        if(srcArray.length>0) {
            for(int m = src.length()-1; m>=0; m--){
                char temp1 = destArray[m];
                int index = dedicatedToMatthewChow(srcArray,temp1,count);

                for(int i = index; i<m; i++){
                    listOfSwap.add(new Swap(i, i+1));
                    char temp2 = srcArray[i];

                    srcArray[i]= srcArray[i+1];
                    srcArray[i+1]= temp2;

                    if(srcArray==destArray){
                        return listOfSwap;
                    }
                }
                count++;
            }
        }


        return listOfSwap;

    }

    /**
     * Finds a list of valid flips on src that when applied to src gives dest.
     * Throws a NoFlipListException if no such list of flips exists.
     *
     * @param src
     * @param dest
     * @return A list of flips, if any exist, that results in dest when applied
     *     to src
     * @throws NoFlipListException if there does not exist a sequence of flips
     *     that results in dest when applied to src
     */
    public static List<Swap> flipsSequence(String src, String dest) throws NoFlipListException {
        char[] srcArray = src.toCharArray();
        char[] destArray = dest.toCharArray();
        List<Swap> listOfSwap = new ArrayList<>();
        boolean[] checked = new boolean[srcArray.length];
        boolean state;

        for(int i = 0; i<src.length(); i++){
            checked[i]=false;
        }

        if(srcArray.length != destArray.length){
            throw new NoFlipListException();
        }



        for(int i = 0; i<src.length(); i++){
            char temp = src.charAt(i);
            state = false;

            for(int m = 0; m<src.length();m++){
                if(temp==dest.charAt(m) && !checked[m] && !state){
                    checked[m] = true;
                    state = true;

                }
            }

            if(!state){
                throw new NoFlipListException();
            }
        }


        if(srcArray.length>0) {
            for(int m = src.length()-1; m>=0; m--){
                char temp1 = destArray[m];
                int index = findIndex(srcArray, temp1);

                for(int i = index; i<m; i++){
                    listOfSwap.add(new Swap(i, i+1));
                    char temp2 = srcArray[i];

                    srcArray[i]= srcArray[i+1];
                    srcArray[i+1]= temp2;
                }
            }
        }


        return listOfSwap;
    }


    public static int findIndex(char arr[], char t)
    {
        int len = arr.length;
        return IntStream.range(0, len)
                .filter(i -> t == arr[i])
                .findFirst() // first occurrence
                .orElse(-1); // No element found
    }



    /**
     * Determines the number of pairs of distinct substrings of s that are
     *     a distance of maxDist or less.
     * The distance of a pair of strings is defined to be the distance between
     *     its two constituent strings.
     * The distance between two strings is defined to be the length of the
     *     shortest list of (valid on either string) flips that brings one
     *     string to the other. If no such list exists, the distance is
     *     taken to be infinite.
     *
     * Note that the distance between two strings as we have defined is
     *     a metric.
     *
     * Further note that a substring may appear multiple times in a string.
     *
     * @param s
     * @param maxDist -- is nonnegative
     * @return the number of pairs of substrings of s whose distance is at
     *     most maxDist.
     */
    public static int similarPairsCount(String s, int maxDist) {
        List<String> allSubstrings = new ArrayList<>();
        char[] string = s.toCharArray();
        int numOfFlips;
        int count = 0;
        int j;
        String temp;

        if(maxDist == 0){
            return 0;
        }

        int n = string.length;
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {

                j = i + len - 1;
                temp = s.substring(i,j+1);

                if(!allSubstrings.contains(temp)){
                    allSubstrings.add(temp);
                }

            }
        }

        for(int i = 0; i< allSubstrings.size(); i++){
            for(int m = i+1; m< allSubstrings.size(); m++){

                    if (isThereAValidWay(allSubstrings.get(i), allSubstrings.get(m))) {
                        int size = flipsSequenceRedone(allSubstrings.get(i), allSubstrings.get(m)).size();
                        if (size <= maxDist) {
                            count++;
                    }
                }
            }
        }

        return count;
    }

}
