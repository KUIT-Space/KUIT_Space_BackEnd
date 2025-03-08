package space.space_spring.domain.discord.domain;

public enum DiscordRole {
    SPACE_MANAGER("Space_Manager",165);
    private String roleName;
    private Integer roleColor;

    private DiscordRole(String roleName,Integer rgb){
        this.roleName=roleName;
    }
    @Override
    public String toString(){
        return this.roleName;
    }
    public Integer getColor(){
        return this.roleColor;
    }
}
