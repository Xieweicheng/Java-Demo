package cn.mrxiexie.spi;

/**
 * @author mrxiexie
 * @date 9/10/19 9:29 PM
 */
public class BananaSPI implements ServiceProviderInterface {

    @Override
    public String getName() {
        return "BananaSPI";
    }
}
