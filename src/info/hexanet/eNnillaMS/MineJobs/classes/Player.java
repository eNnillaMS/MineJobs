package info.hexanet.eNnillaMS.MineJobs.classes;
import java.util.List;
import java.util.UUID;
public class Player {
    public String Name; //IgnoreCase
    public UUID UUID;
    public List<String> Jobs; //UPPERCASE
    public List<String> Invites; //UPPERCASE

    public Player(String name, List<String> jobs, List<String> invites){
        Name = name;
        UUID = new UUID(0,0).fromString(name);
        Jobs = jobs;
        Invites = invites;
    }
}