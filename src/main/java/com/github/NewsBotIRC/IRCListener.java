package com.github.NewsBotIRC;

import com.github.NewsBotIRC.cmds.Cmd;
import com.github.NewsBotIRC.cmds.CmdFactory;
import java.util.List;
import java.util.stream.Stream;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

public class IRCListener extends ListenerAdapter
{
    private final IRCMediator mediator;

    public IRCListener(IRCMediator mediator)
    {
        this.mediator = mediator;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event)
    {
        if (!event.getMessage().startsWith("!")) return;

        String cmdTrigger = this.getTrigger(event.getMessage(), true);
        String cmdParams = this.removeTriggerString(event.getMessage());
        List<Cmd> cmds = CmdFactory.getInstance().getCmds();

        Stream<Cmd> actionableCmds =
                cmds.stream().filter(c -> c.get().equals(cmdTrigger));
        actionableCmds.forEach(c -> c.action(this.mediator, cmdParams));
    }

    public void onConnect(ConnectEvent event)
    {
        if (event.getBot().isNickservIdentified()) return;
        event.getBot().send().identify(
                ConfReader.getInstance().getNickserv_passwd());
    }

    private String getTrigger(String text, boolean skipCmdTrigger)
    {
        String trigger = new String();

        int i = skipCmdTrigger == true ? 1 : 0;
        for (;i < text.length(); ++i) {
            char c = text.charAt(i);

            if (!Character.isLetterOrDigit(c)) break;
            trigger += c;
        }

        return trigger;
    }

    private String removeTriggerString(String text)
    {
        return text.substring(this.getTrigger(text, true).length() + 1,
                text.length()).trim();
    }
}
