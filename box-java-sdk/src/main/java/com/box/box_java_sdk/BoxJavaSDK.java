package com.box.box_java_sdk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
  /*  private static final String CLIENT_ID1 = "qs9oulna0fakkkxjorbvc0bpv8113423";
    private static final String CLIENT_SECRET1 = "tAtjbtmbkIp5n6vQsWBeX1SyOoYVq66M";
    private static final String CLIENT_ID2 = "awpbu85jgxxhfeybsqygf6qqxtn5dbs1";
    private static final String CLIENT_SECRET2 = "VhTpmy4VMhCgyuhm302xYuIwxb6xMZML";*/  
    private static final int MAX_DEPTH = 2;
    private static String Path_id=""; 
    private BoxJavaSDK() { }
    public static void main(String[] args) throws IOException {
    	String arg2 = args[1];
    	String arg1 = args[0];
    	int NoBox = Integer.parseInt(arg2);
    	String ClientID = Readfilenumber("/home/hadoop/TESAPI/TESTSCRIPT/cliID.box",NoBox-1);
    	String ClientSecret = Readfilenumber("/home/hadoop/TESAPI/TESTSCRIPT/cliSECRET.box",NoBox-1);    	
    	String DEVELOPER_TOKEN = (String)Readfile("/home/hadoop/TESAPI/TESTSCRIPT/token.box"+arg2);
    	String REFRESH_TOKEN = (String)Readfile("/home/hadoop/TESAPI/TESTSCRIPT/refreshtoken.box"+arg2);
    	//String DEVELOPER_TOKEN = "PXBFLeUmuu6Xr9rHomHxmzY0ITJnD9xN";
    	//String REFRESH_TOKEN = "znG9URUDDOJ0kWkYgcxw3IGx1cwbo3wDUrILRhEMrqL93hLCDTyaeBn1lNET20Pz";
        Logger.getLogger("com.box.sdk").setLevel(Level.OFF); // Turn off logging to prevent polluting the output.        
        BoxAPIConnection api = new BoxAPIConnection(ClientID.trim(),ClientSecret.trim(),DEVELOPER_TOKEN.trim(),REFRESH_TOKEN.trim());                       
        if(arg1.equals("help")){
        	HelpApi();
        }
        else if(arg1.equals("account")){
        	GetUserInfo(api);
        }
        else if(arg1.equals("space")){
        	space(api);
        }
        else if(arg1.equals("spaceper")){
        	spaceper(api);
        }
        else if(arg1.equals("listingAll")){
        	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
        	listFolder(rootFolder,0);
        }

        else if(arg1.equals("delete")){		
			String arg3 = args[2];
			DeleteFile(api,arg3);
		}
        else if(arg1.equals("metadata")){		
			String arg3 = args[2];
			GetInfo(api,arg3);
		}
        else if(arg1.equals("metadatadir")){
        	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
		   	String arg3 = args[2];
		   	listFolder2(rootFolder, 0, arg3);
		}
        else if(arg1.equals("download")){		
			String arg3 = args[2];
			String arg4 = args[3];
			DownloadFile(api, arg3,arg4);
		}
        else if(arg1.equals("download2")){		
			String arg3 = args[2];
			String arg4 = args[3];
			DownloadFile2(api, arg3,arg4);
		}
        else if(arg1.equals("upload")){        	
			String arg3 = args[2];
			String arg4 = args[3];
			UploadFile(api, arg3 ,arg4);			
		}
        else if(arg1.equals("rename")){		
			String arg3 = args[2];
			String arg4 = args[3];
			Rename(api, arg3,arg4);
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
    	System.out.println("- help			<No.account>{Show more informaion command}");
    	System.out.println("- account		<No.account>{Show account informaion }");
    	System.out.println("- space			<No.account>{Show space informaion }");
    	System.out.println("- spaceper		<No.account>{Show space in percent }");
    	System.out.println("- listingAll		<No.account>{Show all file and Directory}");
    	System.out.println("- delete		<No.account> <id> {Delete file with id}");    	  
    	System.out.println("- metadata		<No.account> <id> {Show file information with id}");    	
    	System.out.println("- download		<No.account> <id> {Download file with id}");
    	System.out.println("- upload		<No.account> <Path1> <Path2> {Upload file with from Path1 to Path2}");    	
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
    private static String Readfilenumber(String ChFile,int linenumber) throws IOException{    	
    	String content = null;    
    	FileInputStream fs= new FileInputStream(ChFile);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fs));
    	for(int i = 1; i <=linenumber; i++)
    	  br.readLine();
    	content = br.readLine();
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
    	System.out.format("total = %d Gb or %d Mb\n",userInfo.getSpaceAmount()/1073741824,userInfo.getSpaceAmount()/1048576);
    	System.out.format("used = %d Gb or %d Mb\n",userInfo.getSpaceUsed()/1073741824,userInfo.getSpaceUsed()/1048576);
    	System.out.format("free = %d Gb or %d Mb\n",(userInfo.getSpaceAmount()/1073741824)-(userInfo.getSpaceUsed()/1073741824),(userInfo.getSpaceAmount()/1048576)-(userInfo.getSpaceUsed()/1048576));
    }
    private static void spaceper(BoxAPIConnection api) throws IOException{
    	BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
    	long total = userInfo.getSpaceAmount()/1073741824;
    	long use = userInfo.getSpaceUsed()/1073741824;
    	long percents = use/total*100;    	
    	System.out.println(percents);
    }
    private static void DownloadFile(BoxAPIConnection api,String id,String path) throws IOException{    	
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();    	
    	FileOutputStream stream = new FileOutputStream(path+info.getName().replaceAll("!", "/").trim());
    	file.download(stream);
    	System.out.println("Download file Completed to "+path+info.getName().replaceAll("!", "/"));
    	stream.close();    	
    }
    private static void DownloadFile2(BoxAPIConnection api,String id,String path) throws IOException{      	 
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();
    	String Name = info.getName().replaceAll("!", "/").trim();
    	String fileName = Name.substring(Name.lastIndexOf("/")+1);
    	FileOutputStream stream = new FileOutputStream(path+fileName);
    	file.download(stream);
    	System.out.println("Download file Completed to "+path+" "+Name);
    	stream.close();    	
    }
    private static void UploadFile(BoxAPIConnection api,String Path , String newPath) throws IOException{
    	BoxFolder rootFolder = BoxFolder.getRootFolder(api);
    	FileInputStream stream = new FileInputStream(Path);
    	rootFolder.uploadFile(stream, Path);    	
    	String fileName = Path.substring(Path.lastIndexOf("/")+1);    	
    	listFolder2(rootFolder,0,fileName);    	
    	System.out.println("Upload file Completed "+fileName+" "+ Path_id);    	
    	Rename(api,(String)newPath.replaceAll("/", "!").trim(),Path_id);    				
    	stream.close();
    }
    private static void Rename(BoxAPIConnection api,String Path,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.new Info();
    	info.setName(Path);
    	file.updateInfo(info);
    }
    private static void DeleteFile(BoxAPIConnection api,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();
    	file.delete();
    	System.out.println("Delete Complete: "+info.getName());
    }
    private static void GetInfo(BoxAPIConnection api,String id) throws IOException{
    	BoxFile file = new BoxFile(api, id);
    	BoxFile.Info info = file.getInfo();
    	System.out.println("Title: " + info.getName());             
        System.out.println("Size: " + info.getSize() + " btye");
        System.out.println("Date: " + info.getCreatedAt());    	    
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
    private static void listFolder2(BoxFolder folder, int depth,String file) {
        for (BoxItem.Info itemInfo : folder) {                        
            if(itemInfo.getName().equals(file)) {Path_id=itemInfo.getID();break;}            
            if (itemInfo instanceof BoxFolder.Info) {
                BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
                if (depth < MAX_DEPTH) {
                    listFolder2(childFolder, depth + 1, file);
                }
            }
        }       
    }



}