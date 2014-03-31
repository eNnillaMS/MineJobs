package info.hexanet.eNnillaMS.MineJobs.classes;
import org.bukkit.configuration.file.YamlConfiguration;
public class Lang {
    public String[] ActionSuccess;
    public String[][] CommandOutput;
    public String[] GeneralErrors;
    public String[] ConfigErrors;
    
    public Lang(YamlConfiguration lang) throws NullPointerException {
        try {
            ActionSuccess = new String[]{
                get(lang, "actions.BlockBroken"),//0
                get(lang, "actions.BlockPlaced"),
                get(lang, "actions.MobKilled"),
                get(lang, "actions.FishCaught"),
                get(lang, "actions.ItemCrafted"),
                get(lang, "actions.ItemSmelted"),//5
                get(lang, "actions.PotionBrewed"),
                get(lang, "actions.EnchantComplete"),
                get(lang, "actions.SignCreate"),
                get(lang, "actions.SignDelete")
            };
            CommandOutput = new String[][]{
                {//0 - getJob
                    get(lang, "commands.getJob.cu"),//0
                    get(lang, "commands.getJob.success"),
                    get(lang, "commands.getJob.successOther"),
                    get(lang, "commands.getJob.TooManyPeople"),
                    get(lang, "commands.getJob.TooManyJobs"),
                    get(lang, "commands.getJob.AlreadyHave"),//5
                    get(lang, "commands.getJob.LockedJob"),
                    get(lang, "commands.getJob.TooManyJobsOther"),
                    get(lang, "commands.getJob.AlreadyHaveOther"),
                    get(lang, "commands.getJob.LockedJobOther")
                }, {//1 - quitJob
                    get(lang, "commands.quitJob.cu"),//0
                    get(lang, "commands.quitJob.success"),
                    get(lang, "commands.quitJob.successOther"),
                    get(lang, "commands.quitJob.DontHaveJob"),
                    get(lang, "commands.quitJob.DontHaveJobOther"),
                    get(lang, "commands.quitJob.ForcedJob"),//5
                }, {//2 - listJobs
                    get(lang, "commands.listJobs.YourJobs"),//0
                    get(lang, "commands.listJobs.QuitInst"),
                    get(lang, "commands.listJobs.Availible"),
                    get(lang, "commands.listJobs.Invite"),
                    get(lang, "commands.listJobs.GetInst"),
                    get(lang, "commands.listJobs.lbl"),//5
                    get(lang, "commands.listJobs.admn"),
                    get(lang, "commands.listJobs.cust"),
                    get(lang, "commands.listJobs.lock")
                }, {//3 - info
                    get(lang, "commands.info.cu"),//0
                    get(lang, "commands.info.JobName"),
                    get(lang, "commands.info.Owner"),
                    get(lang, "commands.info.Locked"),
                    get(lang, "commands.info.DeathLosses"),
                    get(lang, "commands.info.Max"),//5
                    get(lang, "commands.info.Worlds"),
                    get(lang, "commands.info.Payouts"),
                    get(lang, "commands.info.Break"),
                    get(lang, "commands.info.Place"),
                    get(lang, "commands.info.Kill"),//10
                    get(lang, "commands.info.Fish"),
                    get(lang, "commands.info.Tools"),
                    get(lang, "commands.info.Craft"),
                    get(lang, "commands.info.Smelt"),
                    get(lang, "commands.info.Brew"),//15
                    get(lang, "commands.info.Enchant")
                }, {//4 - MainHelp
                    get(lang, "commands.help.getJob"),//0
                    get(lang, "commands.help.quitJob"),
                    get(lang, "commands.help.myJobs"),
                    get(lang, "commands.help.listJobs"),
                    get(lang, "commands.help.info"),
                    get(lang, "commands.help.mja"),//5
                    get(lang, "commands.help.mjc"),
                    get(lang, "commands.help.more")
                }, {//5 - create
                    get(lang, "commands.create.cu"),//0
                    get(lang, "commands.create.success")
                }, {//4 - upgrade
                    get(lang, "commands.upgrade.cu"),//0
                    get(lang, "commands.upgrade.success")
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }, {//4 - MainHelp
                }
            };
            GeneralErrors = new String[]{
                get(lang, "mainErrors.NoVault"),//0
                get(lang, "mainErrors.CustomsDisabledWarning"),
                get(lang, "mainErrors.NoPermissions"),
                get(lang, "mainErrors.JobNotFound"),
                get(lang, "mainErrors.JobExists"),
                get(lang, "mainErrors.ItemNotFound"),//5
                get(lang, "mainErrors.ItemExists"),
                get(lang, "mainErrors.WorldNotListed"),
                get(lang, "mainErrors.WorldExists"),
                get(lang, "mainErrors.BadCMD"),
                get(lang, "mainErrors.PlayerNotFound")//10
            };
            ConfigErrors = new String[]{
                get(lang, "configErrors.SaveFail"),//0
                get(lang, "configErrors.BadLangFile"),
                get(lang, "configErrors.BadLocale"),
                get(lang, "configErrors.BadUseSigns"),
                get(lang, "configErrors.BadUseCustoms"),
                get(lang, "configErrors.BadSMP"),//5
                get(lang, "configErrors.BadCMDYN"),
                get(lang, "configErrors.BadDebug"),
                get(lang, "configErrors.BadPKLYN"),
                get(lang, "configErrors.BadPKLloss"),
                get(lang, "configErrors.BadmaxJobs"),//10
                get(lang, "configErrors.BadDefaults"),
                get(lang, "configErrors.BadForcedList"),
                get(lang, "configErrors.CantLoadConfig"),
                get(lang, "configErrors.PlayerJobNotFound"),
                get(lang, "configErrors.PlayerInvNotFound"),//15
                get(lang, "configErrors.CantLoadPlayers"),
                get(lang, "configErrors.JobItemFail"),
                get(lang, "configErrors.BadEnchValue"),
                get(lang, "configErrors.CantLoadJobs"),
                get(lang, "configErrors.CantLoadCustoms"),//20
                get(lang, "configErrors.NoPayment"),
                get(lang, "configErrors.NoWorlds"),
                get(lang, "configErrors.NoOwner"),
                get(lang, "configErrors.BrokenSign"),
                get(lang, "configErrors.CantLoadSigns"),//25
                get(lang, "configErrors.BackupError")
            };
        } catch (NullPointerException ex){
            throw ex;
        }
    }
    
    private String get(YamlConfiguration lang, String path) throws NullPointerException{
        if (lang.getString(path) == null) throw new NullPointerException();
        else return lang.getString(path);
    }
}