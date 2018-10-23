package com.example.android.iread;


/**
 * {@link list_class} represents a single  book and its specifications.
 * <p>
 * Each object has 3 properties: image resource ID, name of the book and its authos name.
 */

public class list_class {

    // variable for name of the book.
    private String mBookName;

    // variable for name of the author.
    private String mAuthorName;

    // variable for Image associated with the book object
    private String mImage;

    // variable for downloading a book of type pdf;
    private String mpdfLink;

    // variable to get total number of pages  ;
    private String mPages;

    // variable to the date when the book was published l
    private String mPublishedDate;

    // variable to give details about the book;
    private String mdetails;

    // variable for name of the pdf
    private String mpdfname;


    /*
    * constructor for list_class object
    *
    * @param Name is the name of the book
    * @param Author is the name of the author
    * @param imageName is drawable reference ID that corresponds to the Android version
    * */

    public list_class(String Name, String Author, String imageName, String pages, String publishedDate, String details,  String pdfLink, String pdfName){;//}  String details  String imageName, String details){//},String pdfLink,, String epubLink) {
        mBookName = Name;
        mAuthorName = Author;
        mImage = imageName;
        mPages = pages;
        mPublishedDate = publishedDate;
        mdetails = details;
        mpdfLink = pdfLink;
        mpdfname = pdfName;
    }

    /**
     * Get the name of the Book
     */

    public String getmBookName() {
        return mBookName;
    }

    /**
     * Get the name of the author of the book
     */

    public String getmAuthorName() {
        return mAuthorName;
    }

    /**
     * Get the image associated with the book
     */

    public String  getmImage() {
        return mImage;
    }

    /**
     * Get the pdfLink to download
     */
    public String getMpdfLink() {
        return mpdfLink;
    }

    /**
     * Get total  number of pages in a book
     */
    public String getmPages() {
        return mPages;
    }
//
    /**
     * Get the date when the book was published
     */
   public String getmPublishedDate() {
       return mPublishedDate;
   }


    public String getmdetails(){
        return mdetails;
    }

    /**
     * Get the epubLink to download
     */
    //public String getMepubLink() {
   //     return mepubLink;
  //  }

  //  public String getMpdfLink(){
    //    return mpdfLink;
   // }

    public String getMpdfName(){ return mpdfname;}

}










