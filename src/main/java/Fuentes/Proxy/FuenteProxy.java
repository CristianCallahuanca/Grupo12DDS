package Fuentes.Proxy;

import Fuentes.Fuente;

public abstract class FuenteProxy extends Fuente {
    public abstract void sincronizar(); // cada fuente externa sincroniza diferente
}
