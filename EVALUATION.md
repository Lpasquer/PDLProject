# JUnit tests

* BenchTest : JUnit Tests work perfectly, the csv file is well created and we find the tables extracted in the "output" folder

* ExtractorTest : Test on Wikitext extractor don't work. the problem lies in the compareColLine method which compares the number of &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; column and line of each tables found with the number of column and line of tables in the wikipedia link

* PageCheckerTest : The test existingPagesTest_withHttps() work but not with current url, because of some problem with git i can't add good &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; url now. Same thing for existingPagesTest_WithoutHttps().

* PageTest : -JUnit Tests work perfectly, some links (31 on 336) in the array no longer exist, that's why the getUrl () function returns "An error &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; occurred while connecting to the page (null)".  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -getTitleWithoutSpace() isn't representative, it test just one precise case. By the way, it don't work if there is an accent in the title.  

* SavePathTest : **Take a look at the issues associate**

# Manual tests

We also did manual testing on links from wikipedia pages and other sites not listed in the table. For each wikipedia link, the software finds the correct number of tables in the page and for the other links, it detects that it is not a wikipedia page
