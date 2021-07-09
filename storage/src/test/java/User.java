import com.celeste.databases.storage.model.annotation.Key;
import com.celeste.databases.storage.model.annotation.Storable;
import java.util.UUID;

@Storable("elo_user")
public class User {

  @Key
  private final UUID id;
  private String name;

  private int elo;
  private int killStreak;

  private User() {
    this(null, null);
  }

  public User(final UUID id, final String name) {
    this(id, name, 0);
  }

  public User(final UUID id, final String name, final int elo) {
    this(id, name, elo, 0);
  }

  public User(final UUID id, final String name, final int elo, final int killStreak) {
    this.id = id;
    this.name = name;
    this.elo = elo;
    this.killStreak = killStreak;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getElo() {
    return elo;
  }

  public void setElo(final int elo) {
    this.elo = elo;
  }

  public int getKillStreak() {
    return killStreak;
  }

  public void setKillStreak(final int killStreak) {
    this.killStreak = killStreak;
  }

}