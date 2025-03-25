package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonInteractionProcessor {
    boolean supports(String buttonId);
    void process(ButtonInteractionEvent event);
}
