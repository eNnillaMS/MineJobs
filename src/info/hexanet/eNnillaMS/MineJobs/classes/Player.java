package info.hexanet.eNnillaMS.MineJobs.classes;
import java.util.List;
public class Player {
    public String Name; //IgnoreCase
    public List<String> Jobs; //UPPERCASE
    public List<String> Invites; //UPPERCASE

    public Player(String name, List<String> jobs, List<String> invites){
        Name = name;
        Jobs = jobs;
        Invites = invites;
    }
}
