package cn.mrxiexie.spi;

/**
 * @author mrxiexie
 * @date 9/10/19 9:29 PM
 */
public class AppleSPI implements ServiceProviderInterface {

    @Override
    public String getName() {
        return "AppleSPI";
    }
}
