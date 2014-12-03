package org.bb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.bb.ActionReturn;

public class FileEditOwner {
	public Path destination;
	public AclEntryType permission;
	public UserPrincipal user;
	
	public FileEditOwner(Path destination, String user) throws IOException
	{
		super();
		this.destination = destination;
		try {
			this.user = destination.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(user);
		} catch (UserPrincipalNotFoundException e) {
			// TODO: handle exception
		}
			
	}
	
	public ActionReturn doWork(Path destination)
	{
		try {
			Files.setOwner(destination, user);
		} catch (IOException e) {
			return new ActionReturn(destination, false); 
		}
		return new ActionReturn(destination, true);   
	}
}
