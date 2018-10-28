package com.newsapp.npmain.newsapptwo;

public class NewsReport
{
    private String title = "";
    private String sectionName = "";
    private String webUri ="";
    private String authorName = "";

    private String datePublished = "";

    public NewsReport() {

    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }

    public String getWebUri()
    {
        return webUri;
    }

    public void setWebUri(String webUri)
    {
        this.webUri = webUri;
    }

    public String getDatePublished()
    {
        return datePublished;
    }

    public void setDatePublished(String datePublished)
    {
        this.datePublished = datePublished;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorFirstName, String authorLastName, String notAvailable)
    {
        if (authorFirstName.equals(notAvailable) && authorLastName.equals( notAvailable))
        {
            this.authorName = notAvailable;
        }
        else if (authorFirstName.equals(notAvailable))
        {
            this.authorName = authorLastName;
        }
        else if (authorLastName.equals(notAvailable))
        {
            this.authorName = authorFirstName;
        }
        else
        {
            this.authorName = authorFirstName + " " + authorLastName;
        }

    }
}
