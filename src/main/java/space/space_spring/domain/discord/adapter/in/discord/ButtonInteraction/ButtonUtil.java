package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ButtonUtil {
    public List<ActionRow> partitionButtons(List<Button> buttons) {
        final int MAX_BUTTONS_PER_ROW = 5;
        List<ActionRow> rows = new java.util.ArrayList<>();
        for (int i = 0; i < buttons.size(); i += MAX_BUTTONS_PER_ROW) {
            rows.add(ActionRow.of(buttons.subList(i, Math.min(i + MAX_BUTTONS_PER_ROW, buttons.size()))));
        }
        return rows;
    }
}
