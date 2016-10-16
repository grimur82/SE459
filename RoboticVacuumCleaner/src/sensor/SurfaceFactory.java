package sensor;

public class SurfaceFactory {
	public static Surface getSurface(String type, int x, int y){
		if(type.equals("bare")){
			return new SurfaceBareImpl(type,x,y);
		}
		if(type.equals("low-pile")){
			return new SurfaceLowPileImpl(type,x,y);
		}
		if(type.equals("high-pile")){
			return new SurfaceHighPileImpl(type,x,y);
		}
		return null;
	}
}