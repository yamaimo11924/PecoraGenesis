package your.domain.minecraft.pecoraGenesis.gene;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

public class GeneDatabase {

    private final JavaPlugin plugin;
    private Connection connection;

    // 対象動物
    private static final Set<EntityType> TARGET_TYPES = Set.of(
            EntityType.COW,
            EntityType.PIG,
            EntityType.SHEEP,
            EntityType.RABBIT
    );

    // 日付フォーマット（再利用）
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss");

    // SQL定数
    private static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS genes (
            uuid TEXT PRIMARY KEY,
            type TEXT,
            gene TEXT,
            date TEXT,
            world TEXT,
            x INTEGER,
            y INTEGER,
            z INTEGER,
            alive INTEGER
        );
        """;

    private static final String CREATE_INDEX_SQL =
            "CREATE INDEX IF NOT EXISTS idx_alive ON genes(alive);";

    private static final String SAVE_SQL = """
        INSERT OR REPLACE INTO genes
        (uuid,type,gene,date,world,x,y,z,alive)
        VALUES (?,?,?,?,?,?,?,?,1)
        """;

    private static final String DEAD_SQL =
            "UPDATE genes SET alive = 0 WHERE uuid = ?";

    private static final String LOAD_SQL =
            "SELECT gene FROM genes WHERE uuid = ? AND alive = 1";

    public GeneDatabase(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // DB接続
    private void connect() {

        try {

            File dbFile = new File(plugin.getDataFolder(), "gene.db");

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            // SQLite高速化
            try (Statement stmt = connection.createStatement()) {

                stmt.execute("PRAGMA journal_mode=WAL;");
                stmt.execute("PRAGMA synchronous=NORMAL;");
                stmt.execute("PRAGMA temp_store=MEMORY;");
                stmt.execute("PRAGMA foreign_keys=ON;");

            }

        } catch (SQLException e) {
            plugin.getLogger().severe("SQLite connection failed: " + e.getMessage());
        }
    }

    // テーブル作成
    private void createTable() {

        if (connection == null) return;

        try (Statement stmt = connection.createStatement()) {

            stmt.execute(CREATE_TABLE_SQL);
            stmt.execute(CREATE_INDEX_SQL);

        } catch (SQLException e) {
            plugin.getLogger().severe("Table creation failed: " + e.getMessage());
        }
    }

    /*
     生存個体保存
     */
    public void save(LivingEntity entity, Gene gene) {

        if (connection == null) return;

        EntityType type = entity.getType();

        if (!TARGET_TYPES.contains(type)) {
            return;
        }

        Location loc = entity.getLocation();

        String date = LocalDateTime.now().format(DATE_FORMAT);

        try (PreparedStatement ps = connection.prepareStatement(SAVE_SQL)) {

            ps.setString(1, entity.getUniqueId().toString());
            ps.setString(2, type.name());
            ps.setString(3, gene.toString());
            ps.setString(4, date);
            ps.setString(5, loc.getWorld() != null ? loc.getWorld().getName() : "world");
            ps.setInt(6, loc.getBlockX());
            ps.setInt(7, loc.getBlockY());
            ps.setInt(8, loc.getBlockZ());

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe("Save gene failed: " + e.getMessage());
        }
    }

    /*
     死亡個体へ移動
     */
    public void markDead(UUID uuid) {

        if (connection == null) return;

        try (PreparedStatement ps = connection.prepareStatement(DEAD_SQL)) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().severe("Mark dead failed: " + e.getMessage());
        }
    }

    /*
     読み込み
     */
    public Gene load(UUID uuid) {

        if (connection == null) return null;

        try (PreparedStatement ps = connection.prepareStatement(LOAD_SQL)) {

            ps.setString(1, uuid.toString());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return Gene.fromString(rs.getString("gene"));
                }

            }

        } catch (SQLException e) {
            plugin.getLogger().severe("Load gene failed: " + e.getMessage());
        }

        return null;
    }

    /*
     プラグイン起動時
     */
    public void load() {

        connect();
        createTable();

    }

    /*
     プラグイン停止時
     */
    public void close() {

        try {

            if (connection != null) {
                connection.close();
                connection = null;
            }

        } catch (SQLException e) {
            plugin.getLogger().severe("Database close failed: " + e.getMessage());
        }
    }
}
