package generacion_asm.util;

public class InfoReg {
    private final String tipo;
    private int ref;
    private boolean ocupado;

    @Override
    public String toString() {
        return "InfoReg{" +
                "tipo='" + tipo + '\'' +
                ", ref=" + ref +
                ", ocupado=" + ocupado +
                '}';
    }

    public InfoReg() {
        this.ref = -1;
        this.ocupado = false;
        this.tipo = "UINT";
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public boolean isNotOcupado() {
        return !ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public String getTipo() {
        return tipo;
    }
}
