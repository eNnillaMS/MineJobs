package info.hexanet.eNnillaMS.MineJobs.classes;
import org.bukkit.configuration.file.YamlConfiguration;
public class Lang {
    public String[] ActionSuccess;
    public String[][] CommandOutput;
    public String[] GeneralErrors;
    public String[] ConfigErrors;
    public String[] MineJobOutput;
    private String cu;
    
    public Lang(YamlConfiguration lang) throws NullPointerException {
        try {
            cu = get(lang, "commands.cu");
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
                get(lang, "actions.SignDelete"),
                get(lang, "actions.Interact")//10
            };
            MineJobOutput = new String[]{
                get(lang, "commands.minejobs.vers"),//0
                get(lang, "commands.minejobs.auth"),
                get(lang, "commands.minejobs.sign"),
                get(lang, "commands.minejobs.cust"),
                get(lang, "commands.minejobs.deat"),
                get(lang, "commands.minejobs.cmde"),//5
                get(lang, "commands.minejobs.smpo"),
                get(lang, "commands.minejobs.dbgo"),
                get(lang, "commands.minejobs.defj"),
                get(lang, "commands.minejobs.forj"),
                get(lang, "commands.minejobs.help")//10
            };
            CommandOutput = new String[][]{
                {//0 - getJob
                    cu + get(lang, "commands.getJob.cu"),//0
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
                    cu + get(lang, "commands.quitJob.cu"),//0
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
                    cu + get(lang, "commands.info.cu"),//0
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
                    cu + get(lang, "commands.create.cu"),//0
                    get(lang, "commands.create.success")
                }, {//6 - upgrade
                    cu + get(lang, "commands.upgrade.cu"),//0
                    get(lang, "commands.upgrade.success")
                }, {//7 - delete
                    cu + get(lang, "commands.delete.cu"),//0
                    get(lang, "commands.delete.success")
                }, {//8 - rename
                    cu + get(lang, "commands.rename.cu"),//0
                    get(lang, "commands.rename.success")
                }, {//9 - setmax
                    cu + get(lang, "commands.setmax.cu"),//0
                    get(lang, "commands.setmax.success")
                }, {//10 - addobj
                    cu + get(lang, "commands.addobj.cu"),//0
                    get(lang, "commands.addobj.success"),
                    get(lang, "commands.addobj.MustBeNumber")
                }, {//11 - delobj
                    cu + get(lang, "commands.delobj.cu"),//0
                    get(lang, "commands.delobj.success")
                }, {//12 - editobj
                    cu + get(lang, "commands.editobj.cu"),//0
                    get(lang, "commands.editobj.success"),
                    get(lang, "commands.editobj.MustBeNumber")
                }, {//13 - setenchant
                    cu + get(lang, "commands.setenchant.cu"),//0
                    get(lang, "commands.setenchant.success"),
                    get(lang, "commands.setenchant.MustBeNumber")
                }, {//14 - addworld
                    cu + get(lang, "commands.addworld.cu"),//0
                    get(lang, "commands.addworld.success")
                }, {//15 - rmworld
                    cu + get(lang, "commands.rmworld.cu"),//0
                    get(lang, "commands.rmworld.success")
                }, {//16 - togglePDL
                    cu + get(lang, "commands.togglePDL.cu"),//0
                    get(lang, "commands.togglePDL.success"),
                    get(lang, "commands.togglePDL.success2"),
                    get(lang, "commands.togglePDL.error")
                }, {//17 - setOwner
                    cu + get(lang, "commands.setOwner.cu"),//0
                    get(lang, "commands.setOwner.success")
                }, {//18 - lock
                    cu + get(lang, "commands.lock.cu"),//0
                    get(lang, "commands.lock.success1"),
                    get(lang, "commands.lock.success2")
                }, {//19 - kick
                    cu + get(lang, "commands.kick.cu"),//0
                    get(lang, "commands.kick.success")
                }, {//20 - invite
                    cu + get(lang, "commands.invite.cu"),//0
                    get(lang, "commands.invite.success")
                }, {//21 - aHelp
                    "  " + get(lang, "commands.create.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.create"),
                    "  " + get(lang, "commands.upgrade.cu") + "\n    - " + get(lang, "commands.help.upgrade"),
                    "  " + get(lang, "commands.delete.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.delete"),
                    "  " + get(lang, "commands.rename.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.rename"),
                    "  " + get(lang, "commands.setmax.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.setmax"),
                    "  " + get(lang, "commands.addobj.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.addobj"),
                    "  " + get(lang, "commands.editobj.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.editobj"),
                    "  " + get(lang, "commands.delobj.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.delobj"),
                    "  " + get(lang, "commands.setenchant.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.setench"),
                    "  " + get(lang, "commands.addworld.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.addworld"),
                    "  " + get(lang, "commands.rmworld.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.rmworld"),
                    "  " + get(lang, "commands.togglePDL.cu").replace("%CMD%", "mja") + "\n    - " + get(lang, "commands.help.togglepdl"),
                    "  /mja reload\n    - " + get(lang, "commands.help.reload")
                }, {//22 - cHelp
                    "  " + get(lang, "commands.create.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.create"),
                    "  " + get(lang, "commands.delete.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.delete"),
                    "  " + get(lang, "commands.rename.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.rename"),
                    "  " + get(lang, "commands.setmax.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.setmax"),
                    "  " + get(lang, "commands.addobj.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.addobj"),
                    "  " + get(lang, "commands.editobj.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.editobj"),
                    "  " + get(lang, "commands.delobj.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.delobj"),
                    "  " + get(lang, "commands.setenchant.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.setench"),
                    "  " + get(lang, "commands.addworld.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.addworld"),
                    "  " + get(lang, "commands.rmworld.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.rmworld"),
                    "  " + get(lang, "commands.togglePDL.cu").replace("%CMD%", "mjc") + "\n    - " + get(lang, "commands.help.togglepdl"),
                    "  " + get(lang, "commands.setOwner.cu") + "\n    - " + get(lang, "commands.help.setowner"),
                    "  " + get(lang, "commands.lock.cu") + "\n    - " + get(lang, "commands.help.lock"),
                    "  " + get(lang, "commands.kick.cu") + "\n    - " + get(lang, "commands.help.kick"),
                    "  " + get(lang, "commands.invite.cu") + "\n    - " + get(lang, "commands.help.invite")
                }, {//23 - reload
                    get(lang, "commands.reload.Success"),//0
                    get(lang, "commands.reload.Failed")
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
                get(lang, "mainErrors.PlayerNotFound"),//10
                get(lang, "mainErrors.BadType"),
                get(lang, "mainErrors.BadArg"),
                get(lang, "mainErrors.WorldNotFound")
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