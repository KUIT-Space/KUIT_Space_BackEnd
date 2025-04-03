package space.space_spring.domain.discord.adapter.in.discord;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class TitleAndContentParser {
    private String title;
    private String content;

    private TitleAndContentParser(String title,String content){
        this.title=title;
        this.content=content;
    }

    public static TitleAndContentParser parse(String input){
        if (input == null || input.isBlank()) {
            //
            return new TitleAndContentParser("","");
        }

        String[] lines = input.split("\n", -1);
        String title = "";
        int index = 0;

        // 첫 줄이 비어있으면 다음 줄로 이동
        while (index < lines.length && !validateTitle(lines[index])) {
            index++;
        }

        if (index < lines.length) {
            title = lines[index]; // 첫 번째 유효한 줄을 title로 설정
            index++;
        }

        // 나머지 줄을 content로 결합
        String content = String.join("\n", Arrays.copyOfRange(lines, index, lines.length));

        return new TitleAndContentParser(title,content);
    }

    private static boolean validateTitle(String line){
        if(line.isEmpty()||line.isBlank()){return false;}
        if(line.trim().equalsIgnoreCase("@everyone")){return false;}

        return true;
    }
}
