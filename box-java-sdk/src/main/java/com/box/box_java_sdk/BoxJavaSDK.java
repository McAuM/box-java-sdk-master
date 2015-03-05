package com.box.box_java_sdk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFile.Info;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;
import com.box.sdk.Metadata;
import com.box.sdk.ProgressListener;

public final class BoxJavaSDK {
    private static final String CLIENT_ID1 = "qs9oulna0fakkkxjorbvc0bpv8113423";
    private static final String CLIENT_SECRET1 = "tAtjbtmbkIp5n6vQsWBeX1SyOoYVq66M";
    private static final String CLIENT_ID2 = "awpbu85jgxxhfeybsqygf6qqxtn5dbs1";
    private static final String CLIENT_SECRET2 = "VhTpmy4VMhCgyuhm302xYuIwxb6xMZML";  
    private static final int MAX_DEPTH = 2;
    private BoxJavaSDK() { }
    public static void main(String[] args) throws IOException {
    	String arg2 = args[1];
    	String arg1 = args[0];
    	String ClientID = "";
    	String ClientSecret = "";
    	if(arg1.equals("1")){
    		ClientID = CLIENT_ID1;
    		ClientSecret = CLIENT_SECRET1;
    	}
    	else if (arg1.equals("2")){
    		ClientID = CLIENT_ID2;
    		ClientSecret = CLIENT_SECRET2;
    	}
    	String DEVELOPER_TOKEN = (String)Readfile("token.box"+arg1);
    	String REFRESH_TOKEN = (String)Readfile("refreshtoken.box"+arg1);    	
        Logger.getLogger("com.box.sdk").setLevel(Level.OFF); // Turn off logging to prevent polluting the output.
        BoxAPIConnection api = new BoxAPIConnection(ClientID,ClientSecret,DEVELOPER_TOKEN.trim(),REFRESH_TOKEN.trim());                       
        if(arg2.equals("help")){
        	HelpApi();
        }
        else if(arg2.equals("account")){
        	GetUserInfo(api);
        }
        else if(arg2.equals("space")){
        	space(api);
        }
        else if(arg2.equals("spaceper")){
        	spaceper(api);
        }
        else if(arg2.equals("listingAll")){
        	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        	listFolder(rootFolder,0);
        }
        else if(arg2.equals("listing")){ 
        	String arg3 = args[2];
        	listing(api,arg3);
        }
        else if(arg2.equals("deletefile")){		
			String arg3 = args[2];
			DeleteFile(api,arg3);
		}
        else if(arg2.equals("deletedir")){			
			String arg3 = args[2];
			DeleteFolder(api,arg3);
		}
        else if(arg2.equals("metadatafile")){		
			String arg3 = args[2];
			GetInfo(api,arg3);
		}
        else if(arg2.equals("metadatadir")){		
		   	
		}
        else if(arg2.equals("download")){		
			String arg3 = args[2];
			DownloadFile(api, arg3);
		}
        else if(arg2.equals("upload")){		
			String arg3 = args[2];
			UploadFile(api, arg3);
		}
		else System.out.println("Commnad Error");
    }
    private static void HelpApi() throws IOException{
    	System.out.println("------------------------------------------------------");
    	System.out.println("--#######-#######-##----##---####---#######-########--");
    	System.out.println("--##---##-##---##--##--##---##--##--##---##----##-----");
    	System.out.println("--######--##---##----##----##----##-#######----##-----");
    	System.out.println("--######--##---##----##----########-##---------##-----");
    	System.out.println("--##---##-##---##--##--##--##----##-##---------##-----");
    	System.out.println("--#######-#######-##----##-##----##-##------########--");
    	System.out.println("------------------------------------------------------");
    	System.out.println("Usage Commnad-line");
    	System.out.println("Commmand <need> [option] {detail}");
    	System.out.println("- help			{Show more informaion command}");
    	System.out.println("- account		{Show account informaion }");
    	System.out.println("- space			{Show space informaion }");
    	System.out.println("- spaceper		{Show space in percent }");
    	System.out.println("- listingAll		{Show all file and Directory}");
    	System.out.println("- deletefile		<id> {Delete file with id}");
    	System.out.println("- deletedir		<id> {Delete folder with id}");    	
    	System.out.println("- metadatafile		<id> {Show file information with id}");    	
    	System.out.println("- download		<id> {Download file with id}");
    	System.out.println("- upload		<filename> {Upload file with from filename}");    	
    }
    private static String Readfile(String ChFile) throws IOException{    	
    	String content = null;    
    	   File file = new File(ChFile);
    	   try {
    	       FileReader reader = new FileReader(file);
    	       char[] chars = new char[(int) file.length()];
    	       reader.read(chars);
    	       content = new String(chars);
    	       reader.close();
    	   } catch (IOException e) {
    	       e.printStackTrace();
    	   }
    	   return content;
    }
    private static void GetUserInfo(BoxAPIConnection api) throws IOException{
    	BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
    	System.out.println("User Account Info");
    	System.out.format("user id = %s\n", userInfo.getID());
    	System.out.format("display name = %s\n\n", userInfo.getName());
    	System.out.println("User space");
    	System.out.format("total = %d Gb\n",userInfo.getSpaceAmount()/1073741824);
    	System.out.format("used = %d Gb\n",userInfo.getSpaceUsed()/1073741824);
    	System.out.format("free = %d Gb\n",(userInfo.getSpaceAmount()/1073741824)-(userInfo.getSpaceUsed()/1073741824));
    }
    private static void space(BoxAPIConnection api) throws IOException{
    	BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
    	System.out.println("User space");
    	System.out.format("total = %d Gb\n",userInfo.getSpaceAmount()/1073741824);
    	System.out.format("used = %d Gb\n",userInfo.getSpaceUsed()/1073741824);
    	System.out.format("free = %d Gb\n",(userInfo.getSpaceAmount()/1073741824)-(userInfo.getSpaceUsed()/1073741824));
    }
    private static void spaceper(BoxAPIConnection api) throws IOException{
    	BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
    	long total = userInfo.getSpaceAmount()/1073741824;
    	long use = userInfo.getSpaceUsed()/1073741824;
    	long percents = use/total*100;    	
    	System.out.println(percents);
    }
    private static void DownloadFile(BoxAPIConnection api,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();    	
    	FileOutputStream stream = new FileOutputStream(info.getName());
    	file.download(stream);
    	//System.out.println("Download Complete : "+info.getName());
    	stream.close();
    }
    private static void UploadFile(BoxAPIConnection api,String Path) throws IOException{
    	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
    	FileInputStream stream = new FileInputStream(Path);
    	rootFolder.uploadFile(stream, Path);    	
    	stream.close();
    	//System.out.println("Upload Complete");
    }
    private static void DeleteFile(BoxAPIConnection api,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	file.delete();
    	//System.out.println("Delete Complete: "+file.getInfo().getName());
    }
    private static void GetInfo(BoxAPIConnection api,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();
    	System.out.println(info.getName()+" "+info.getSize());    	
    }
    private static void listFolder(BoxFolder folder, int depth) {
        for (BoxItem.Info itemInfo : folder) {
            String indent = "";
            for (int i = 0; i < depth; i++) {
                indent += "    ";
            }            
            System.out.println(indent  +itemInfo.getName() +" "+itemInfo.getID());
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < MAX_DEPTH) {
                    listFolder(childFolder, depth + 1);
                }
            }
        }
    }
    private static void DeleteFolder(BoxAPIConnection api,String id) throws IOException{
    	BoxFolder folder = new BoxFolder(api, id);
    	folder.delete(true);
    }
    private static void listing(BoxAPIConnection api,String search) throws IOException{
    	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
    	Iterable<BoxItem.Info> results = rootFolder.search(search);
    	for (BoxItem.Info result : results) {
    	    System.out.println(result.getID());
    	}
    }
    
}