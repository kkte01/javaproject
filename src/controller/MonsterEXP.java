package controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MonsterEXP {

		private DoubleProperty exp;

		public double getEXP() {
			if(exp !=  null) {
				return exp.get();
			}else {
				return 0;
			}
		}

		public void setEXP(double exp) {
			this.expProperty().set(exp);
		}
		
		public final DoubleProperty expProperty() {
			if(exp == null ) {
				exp = new SimpleDoubleProperty(0);
			}
			return exp;
		}
}
