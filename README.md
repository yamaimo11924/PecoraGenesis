## PecoraGenesis – Plugin Description

**English:**  
PecoraGenesis adds a deep genetic and livestock management system to Minecraft. Transform wild animals into domesticated livestock with enhanced meat properties, such as status effects and improved hunger restoration. Killing domesticated animals drops special meat, which can be cooked over a campfire to enhance its effects.

Milk collection is limited and influenced by genetic traits, making each cow unique. Livestock data is stored in a database file, allowing persistent tracking of genes and traits. Features include inheritance chance (`inherit_chance`) and dominance chance (`dominance_chance`) for realistic breeding mechanics.

**Japanese (日本語):**  
PecoraGenesisは、Minecraftに遺伝子と家畜管理の深いシステムを追加するプラグインです。野生動物を家畜化することで、ステータス効果や満腹度を強化した肉を手に入れることができます。家畜をキルすると特殊な肉がドロップし、焚火で加熱することでさらに効果を強化できます。

牛乳の採取量には制限があり、遺伝子によって採取量が変化します。家畜データはデータベースファイルに保存され、遺伝子や個体の特徴を永続的に管理可能です。遺伝機能として、`inherit_chance`（遺伝確率）や`dominance_chance`（支配確率）などの要素も含まれています。

---

## Usage / 使い方

### 1. Download / ダウンロード

Download the plugin from the release page or Modrinth.

Release or Modrinth:  
https://modrinth.com/project/pecoragenesis

リリースページまたはModrinthからプラグインをダウンロードしてください。

---

### 2. Breeding Livestock / 家畜の交配

Breed livestock animals:

- Cow (ウシ)
- Sheep (ヒツジ)
- Pig (ブタ)
- Rabbit (ウサギ)

交配すると遺伝子が次の確率で決定されます。

| Type | Chance |
|-----|-----|
| General (AGなど) | **75%** |
| Mutation / inheritance (AA, AG, GGなど) | **20%** |
| Dominance mutation (TG, ACなど) | **5%** |

---

### 3. Check Genes / 遺伝子の確認

Right-click a livestock animal with a **Stone Hoe** to check its gene.

家畜に**石のクワ**を右クリックすると、その個体の遺伝子を確認できます。

---

## Gene Traits / 遺伝子効果

Each gene combination provides different trait bonuses.

| Gene | Drop | Food | Breed | Status |
|-----|-----|-----|-----|-----|
| AA | 0 | 0 | 0 | 0 |
| TA | 0 | 1 | 1 | 0 |
| AT | 0 | 1 | 2 | 0 |
| TT | 0 | 2 | 0 | 0 |
| GA | 0 | 0 | 1 | 0 |
| AG | 1 | 0 | 2 | 0 |
| GG | 3 | 0 | 0 | 0 |
| CA | 0 | 0 | 1 | 1 |
| GT | 2 | 1 | 0 | 0 |
| TG | 1 | 2 | 0 | 0 |
| AC | 0 | 0 | 2 | 1 |
| TC | 0 | 2 | 0 | 1 |
| CT | 0 | 1 | 0 | 2 |
| CG | 1 | 0 | 0 | 2 |
| CC | 0 | 0 | 0 | 3 |
| GC | 2 | 0 | 0 | 1 |

---

## Trait Meaning / 効果の意味

| Trait | Description |
|-----|-----|
| Drop | Meat drop amount (ドロップ量) |
| Food | Hunger restoration (満腹度回復・隠し満腹度回復) |
| Breed | Breeding efficiency (繁殖効率) |
| Status | Status effect strength (ステータス効果) |
