package model;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PoketmonBook1 {
	private int no;
	private String image;
	private String image2;
	private ImageView imageView;
	private String name;
	private String type1;
	private String type2;
	private String hp;
	private String atk;
	private String def;
	private String sAtk;
	private String sDef;
	private String speed;
	private String trait;
	private String height;
	private String weight;
	private String evolve;
	private String info;
	
	public PoketmonBook1(int no, String image, String name, String type1, String type2) {
		this.no = no;
		this.image = image;
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
	}
	

	public PoketmonBook1(int no, ImageView imageView, String name, String type1, String type2) {
		this.no = no;
		this.imageView = imageView;
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
	}
	
	public PoketmonBook1(String image,  String name, String hp, String atk, String def, String sAtk, String sDef, String speed ) {
		this.image = image;
		this.name = name;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.sAtk = sAtk;
		this.sDef = sDef;
		this.speed = speed;
	
	}
	public PoketmonBook1(String image,  String name, String type1, 
			 String hp, String atk, String def, String sAtk, String sDef, String speed, String trait,
			 String height, String weight, String evolve, String info) {
		
		this.image = image;
		this.name = name;
		this.type1 = type1;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.sAtk = sAtk;
		this.sDef = sDef;
		this.speed = speed;
		this.trait = trait;
		this.height = height;
		this.weight = weight;
		this.evolve = evolve;
		this.info = info;
	}

	//더블클릭한 도감에 들어가는 객체 생성자
	public PoketmonBook1(int no, String image2, String name, String type1,
			String type2, String hp, String atk, String def, String sAtk, String sDef, String speed, String trait,
			String height, String weight, String evolve, String info) {
		
		super();
		this.no = no;
		this.image2 = image2;
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.sAtk = sAtk;
		this.sDef = sDef;
		this.speed = speed;
		this.trait = trait;
		this.height = height;
		this.weight = weight;
		this.evolve = evolve;
		this.info = info;
	}


	public int getNo() {
		return no;
	}


	public void setNo(int no) {
		this.no = no;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public ImageView getImageView() {
		return imageView;
	}


	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType1() {
		return type1;
	}


	public void setType1(String type1) {
		this.type1 = type1;
	}


	public String getType2() {
		return type2;
	}


	public void setType2(String type2) {
		this.type2 = type2;
	}


	public String getHp() {
		return hp;
	}


	public void setHp(String hp) {
		this.hp = hp;
	}


	public String getAtk() {
		return atk;
	}


	public void setAtk(String atk) {
		this.atk = atk;
	}


	public String getDef() {
		return def;
	}


	public void setDef(String def) {
		this.def = def;
	}


	public String getsAtk() {
		return sAtk;
	}


	public void setsAtk(String sAtk) {
		this.sAtk = sAtk;
	}


	public String getsDef() {
		return sDef;
	}


	public void setsDef(String sDef) {
		this.sDef = sDef;
	}


	public String getSpeed() {
		return speed;
	}


	public void setSpeed(String speed) {
		this.speed = speed;
	}


	public String getTrait() {
		return trait;
	}


	public void setTrait(String trait) {
		this.trait = trait;
	}


	public String getHeight() {
		return height;
	}


	public void setHeight(String height) {
		this.height = height;
	}


	public String getWeight() {
		return weight;
	}


	public void setWeight(String weight) {
		this.weight = weight;
	}


	public String getEvolve() {
		return evolve;
	}


	public void setEvolve(String evolve) {
		this.evolve = evolve;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public String getImage2() {
		return image2;
	}


	public void setImage2(String image2) {
		this.image2 = image2;
	}


	@Override
	public String toString() {
		return "PoketmonBook1 [no=" + no + ", image=" + image + ", image2=" + image2 
				+ ", name=" + name + ", type1=" + type1 + ", type2=" + type2 + ", hp=" + hp + ", atk=" + atk + ", def="
				+ def + ", sAtk=" + sAtk + ", sDef=" + sDef + ", speed=" + speed + ", trait=" + trait + ", height="
				+ height + ", weight=" + weight + ", evolve=" + evolve + ", info=" + info + "]";
	}
	
	

	
}
