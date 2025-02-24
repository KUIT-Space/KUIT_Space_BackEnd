package space.space_spring.domain.discord.domain;

public enum DiscordRole {
    SPACE_MANAGER("Space_Manager");
    private String roleName;

    private DiscordRole(String roleName){
        this.roleName=roleName;
    }

    public String getRoleName(){
        return this.roleName;
    }
}
