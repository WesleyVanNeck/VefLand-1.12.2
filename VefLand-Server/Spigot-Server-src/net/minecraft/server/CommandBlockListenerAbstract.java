package net.minecraft.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;

// CraftBukkit start
import java.util.ArrayList;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import com.google.common.base.Joiner;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.event.server.ServerCommandEvent;
// CraftBukkit end

public abstract class CommandBlockListenerAbstract implements ICommandListener {

    private static final SimpleDateFormat a = new SimpleDateFormat("HH:mm:ss");
    private long b = -1L;
    private boolean c = true;
    private int d;
    private boolean e = true;
    private IChatBaseComponent f;
    private String g = "";
    private String h = "@";
    private final CommandObjectiveExecutor i = new CommandObjectiveExecutor();
    protected org.bukkit.command.CommandSender sender; // CraftBukkit - add sender

    public CommandBlockListenerAbstract() {}

    public int k() {
        return this.d;
    }

    public void a(int i) {
        this.d = i;
    }

    public IChatBaseComponent l() {
        return (IChatBaseComponent) (this.f == null ? new ChatComponentText("") : this.f);
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Command", this.g);
        nbttagcompound.setInt("SuccessCount", this.d);
        nbttagcompound.setString("CustomName", this.h);
        nbttagcompound.setBoolean("TrackOutput", this.e);
        if (this.f != null && this.e) {
            nbttagcompound.setString("LastOutput", IChatBaseComponent.ChatSerializer.a(this.f));
        }

        nbttagcompound.setBoolean("UpdateLastExecution", this.c);
        if (this.c && this.b > 0L) {
            nbttagcompound.setLong("LastExecution", this.b);
        }

        this.i.b(nbttagcompound);
        return nbttagcompound;
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.g = nbttagcompound.getString("Command");
        this.d = nbttagcompound.getInt("SuccessCount");
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.h = nbttagcompound.getString("CustomName");
        }

        if (nbttagcompound.hasKeyOfType("TrackOutput", 1)) {
            this.e = nbttagcompound.getBoolean("TrackOutput");
        }

        if (nbttagcompound.hasKeyOfType("LastOutput", 8) && this.e) {
            try {
                this.f = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("LastOutput"));
            } catch (Throwable throwable) {
                this.f = new ChatComponentText(throwable.getMessage());
            }
        } else {
            this.f = null;
        }

        if (nbttagcompound.hasKey("UpdateLastExecution")) {
            this.c = nbttagcompound.getBoolean("UpdateLastExecution");
        }

        if (this.c && nbttagcompound.hasKey("LastExecution")) {
            this.b = nbttagcompound.getLong("LastExecution");
        } else {
            this.b = -1L;
        }

        this.i.a(nbttagcompound);
    }

    public boolean a(int i, String s) {
        return i <= 2;
    }

    public void setCommand(String s) {
        this.g = s;
        this.d = 0;
    }

    public String getCommand() {
        return this.g;
    }

    public boolean a(World world) {
        if (!world.isClientSide && world.getTime() != this.b) {
            if ("Searge".equalsIgnoreCase(this.g)) {
                this.f = new ChatComponentText("#itzlipofutzli");
                this.d = 1;
                return true;
            } else {
                MinecraftServer minecraftserver = this.C_();

                if (minecraftserver != null && minecraftserver.M() && minecraftserver.getEnableCommandBlock()) {
                    try {
                        this.f = null;
                        // CraftBukkit start - Handle command block commands using Bukkit dispatcher
                        this.d = executeSafely(this, sender, this.g);
                        // CraftBukkit end
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Executing command block");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Command to be executed");

                        crashreportsystemdetails.a("Command", new CrashReportCallable() {
                            public String a() throws Exception {
                                return CommandBlockListenerAbstract.this.getCommand();
                            }

                            public Object call() throws Exception {
                                return this.a();
                            }
                        });
                        crashreportsystemdetails.a("Name", new CrashReportCallable() {
                            public String a() throws Exception {
                                return CommandBlockListenerAbstract.this.getName();
                            }

                            public Object call() throws Exception {
                                return this.a();
                            }
                        });
                        throw new ReportedException(crashreport);
                    }
                } else {
                    this.d = 0;
                }

                if (this.c) {
                    this.b = world.getTime();
                } else {
                    this.b = -1L;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static int executeSafely(ICommandListener sender, org.bukkit.command.CommandSender bSender, String command) {
        try {
            return executeCommand(sender, bSender, command);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            ChatMessage chatmessage = new ChatMessage(commandexception.getMessage(), commandexception.getArgs());
            chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
            sender.sendMessage(chatmessage);
        }

        return 0;
    }

    // CraftBukkit start
    public static int executeCommand(ICommandListener sender, org.bukkit.command.CommandSender bSender, String command) throws CommandException {
        org.bukkit.command.SimpleCommandMap commandMap = sender.getWorld().getServer().getCommandMap();
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandEvent event = new ServerCommandEvent(bSender, command);
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return 0;
        }
        command = event.getCommand();

        String[] args = command.split(" ");
        ArrayList<String[]> commands = new ArrayList<String[]>();

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        org.bukkit.command.Command commandBlockCommand = commandMap.getCommand(args[0]);
        if (sender.getWorld().getServer().getCommandBlockOverride(args[0])) {
            commandBlockCommand = commandMap.getCommand("minecraft:" + args[0]);
        }
        if (commandBlockCommand instanceof VanillaCommandWrapper) {
            command = command.trim();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }
            String as[] = command.split(" ");
            as = VanillaCommandWrapper.dropFirstArgument(as);
            if (!sender.getWorld().getServer().getPermissionOverride(sender) && !((VanillaCommandWrapper) commandBlockCommand).testPermission(bSender)) {
                return 0;
            }
            return ((VanillaCommandWrapper) commandBlockCommand).dispatchVanillaCommand(bSender, sender, as);
        }

        // Make sure this is a valid command
        if (commandMap.getCommand(args[0]) == null) {
            return 0;
        }

        commands.add(args);

        // Find positions of command block syntax, if any        
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer server = MinecraftServer.getServer();
        server.worldServer = new WorldServer[server.worlds.size()];
        server.worldServer[0] = (WorldServer) sender.getWorld();
        int bpos = 0;
        for (int pos = 1; pos < server.worldServer.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.worldServer[0] == world) {
                pos--;
                continue;
            }
            server.worldServer[pos] = world;
        }
        try {
            ArrayList<String[]> newCommands = new ArrayList<String[]>();
            for (int i = 0; i < args.length; i++) {
                if (PlayerSelector.isPattern(args[i])) {
                    for (int j = 0; j < commands.size(); j++) {
                        newCommands.addAll(buildCommands(sender, commands.get(j), i));
                    }
                    ArrayList<String[]> temp = commands;
                    commands = newCommands;
                    newCommands = temp;
                    newCommands.clear();
                }
            }
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }

        int completed = 0;

        // Now dispatch all of the commands we ended up with
        for (int i = 0; i < commands.size(); i++) {
            try {
                if (commandMap.dispatch(bSender, joiner.join(java.util.Arrays.asList(commands.get(i))))) {
                    completed++;
                }
            } catch (Throwable exception) {
                if (sender.f() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", sender.getChunkCoordinates().getX(), sender.getChunkCoordinates().getY(), sender.getChunkCoordinates().getZ()), exception);
                } else if (sender instanceof CommandBlockListenerAbstract) {
                    CommandBlockListenerAbstract listener = (CommandBlockListenerAbstract) sender;
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().getX(), listener.getChunkCoordinates().getY(), listener.getChunkCoordinates().getZ()), exception);
                } else {
                    MinecraftServer.getServer().server.getLogger().log(Level.WARNING, String.format("Unknown CommandBlock failed to handle command"), exception);
                }
            }
        }

        return completed;
    }

    private static ArrayList<String[]> buildCommands(ICommandListener sender, String[] args, int pos) throws CommandException {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        java.util.List<EntityPlayer> players = (java.util.List<EntityPlayer>)PlayerSelector.getPlayers(sender, args[pos], EntityPlayer.class);

        if (players != null) {
            for (EntityPlayer player : players) {
                if (player.world != sender.getWorld()) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.getName();
                commands.add(command);
            }
        }

        return commands;
    }

    public static CommandSender unwrapSender(ICommandListener listener) {
        org.bukkit.command.CommandSender sender = null;
        while (sender == null) {
            if (listener instanceof DedicatedServer) {
                sender = ((DedicatedServer) listener).console;
            } else if (listener instanceof RemoteControlCommandListener) {
                sender = ((RemoteControlCommandListener) listener).C_().remoteConsole;
            } else if (listener instanceof CommandBlockListenerAbstract) {
                sender = ((CommandBlockListenerAbstract) listener).sender;
            } else if (listener instanceof CustomFunctionData.CustomFunctionListener) {
                sender = ((CustomFunctionData.CustomFunctionListener) listener).sender;
            } else if (listener instanceof CommandListenerWrapper) {
                listener = ((CommandListenerWrapper) listener).base; // Search deeper
            } else if (VanillaCommandWrapper.lastSender != null) {
                sender = VanillaCommandWrapper.lastSender;
            } else if (listener.f() != null) {
                sender = listener.f().getBukkitEntity();
            } else {
                throw new RuntimeException("Unhandled executor " + listener.getClass().getSimpleName());
            }
        }

        return sender;
    }
    // CraftBukkit end

    public String getName() {
        return this.h;
    }

    public void setName(String s) {
        this.h = s;
    }

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        if (this.e && this.getWorld() != null && !this.getWorld().isClientSide) {
            this.f = (new ChatComponentText("[" + CommandBlockListenerAbstract.a.format(new Date()) + "] ")).addSibling(ichatbasecomponent);
            this.i();
        }

    }

    public boolean getSendCommandFeedback() {
        MinecraftServer minecraftserver = this.C_();

        return minecraftserver == null || !minecraftserver.M() || minecraftserver.worldServer[0].getGameRules().getBoolean("commandBlockOutput");
    }

    public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {
        this.i.a(this.C_(), this, commandobjectiveexecutor_enumcommandresult, i);
    }

    public abstract void i();

    public void b(@Nullable IChatBaseComponent ichatbasecomponent) {
        this.f = ichatbasecomponent;
    }

    public void a(boolean flag) {
        this.e = flag;
    }

    public boolean n() {
        return this.e;
    }

    public boolean a(EntityHuman entityhuman) {
        if (!entityhuman.isCreativeAndOp()) {
            return false;
        } else {
            if (entityhuman.getWorld().isClientSide) {
                entityhuman.a(this);
            }

            return true;
        }
    }

    public CommandObjectiveExecutor o() {
        return this.i;
    }
}
