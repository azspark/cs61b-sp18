import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public double ROOT_ULLAT, ROOT_ULLON, ROOT_LRLAT, ROOT_LRLON;
    public int maxDepth;
    public int TILE_SIZE;
    private double[] depthLon;
    private double[] depthLat;
    private double initLonResolution;
    public Rasterer(double ROOT_ULLAT, double ROOT_ULLON, double ROOT_LRLAT, double ROOT_LRLON,
                    int TILE_SIZE, int maxDepth) {
        // YOUR CODE HERE
        this.ROOT_ULLAT = ROOT_ULLAT;
        this.ROOT_ULLON = ROOT_ULLON;
        this.ROOT_LRLAT = ROOT_LRLAT;
        this.ROOT_LRLON = ROOT_LRLON;
        this.TILE_SIZE = TILE_SIZE;
        this.maxDepth = maxDepth;

        depthLon = new double[maxDepth + 1];
        depthLat = new double[maxDepth + 1];
        initLonResolution = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        double recurLon = ROOT_LRLON - ROOT_ULLON;
        double recurLat = ROOT_ULLAT - ROOT_LRLAT;
        for (int i = 0; i <= maxDepth; i++) {
            depthLon[i] = recurLon;
            depthLat[i] = recurLat;
            recurLon /= 2;
            recurLat /= 2;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        if (!validQuery(params)) {
            return invalidOutPut();
        }
        Map<String, Object> results = new HashMap<>();
        int depth = getDepth(params);
        results.put("query_success", true);
        results.put("depth", depth);
        setRender(depth, params, results);
//        System.out.println(results);
        return results;
    }

    private int getDepth(Map<String, Double> params) {
        double lonPP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        int depth = (int) Math.ceil(log2(initLonResolution / lonPP));
        if (depth > maxDepth) {
            depth = maxDepth;
        }
        if (depth < 0) {
            depth = 0;
        }
        return depth;
    }

    private double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    private void setRender(int depth, Map<String, Double> params, Map<String, Object> results) {
        double lonUnit = depthLon[depth];
        double latUnit = depthLat[depth];
        int startLonIdx = (int) Math.floor((params.get("ullon") - ROOT_ULLON) / lonUnit);
        int endLonIdx = (int) Math.floor((params.get("lrlon") - ROOT_ULLON) / lonUnit);
        int startLatIdx = (int) Math.floor((ROOT_ULLAT - params.get("ullat")) / latUnit);
        int endLatIdx = (int) Math.floor((ROOT_ULLAT - params.get("lrlat")) / latUnit);
//        System.out.println("" + startLatIdx + endLatIdx);
        String[][] renderGrid = new String[endLatIdx - startLatIdx + 1][endLonIdx - startLonIdx + 1];
        for (int i = startLatIdx; i <= endLatIdx; i++) {
            for (int j = startLonIdx; j <= endLonIdx; j++) {
                renderGrid[i - startLatIdx][j - startLonIdx] = String.format("d%d_x%d_y%d.png", depth, j, i);
            }
        }
        double rasterUlLon = ROOT_ULLON + startLonIdx * lonUnit;
        double rasterLrLon = ROOT_ULLON + (endLonIdx + 1) * lonUnit;
        double rasterUlLat = ROOT_ULLAT - startLatIdx * latUnit;
        double rasterLrLat = ROOT_ULLAT - (endLatIdx + 1) * latUnit;
        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", rasterUlLon);
        results.put("raster_ul_lat", rasterUlLat);
        results.put("raster_lr_lon", rasterLrLon);
        results.put("raster_lr_lat", rasterLrLat);
    }

    private boolean validQuery(Map<String, Double> params) {
        if (params.get("ullon") > params.get("lrlon")) {
            return false;
        }
        if (params.get("lrlat") > params.get("ullat")) {
            return false;
        }
        if (params.get("lrlat") > ROOT_ULLAT) {
            return false;
        }
        if (params.get("ullat") < ROOT_LRLAT) {
            return false;
        }
        if (params.get("ullon") > ROOT_LRLON) {
            return false;
        }
        if (params.get("lrlon") < ROOT_ULLON) {
            return false;
        }
        return true;
    }

    private Map<String, Object> invalidOutPut() {
        Map<String, Object> results = new HashMap<>();
        results.put("query_success", false);
        results.put("render_grid", null);
        results.put("raster_ul_lon", null);
        results.put("raster_ul_lat", null);
        results.put("raster_lr_lon", null);
        results.put("raster_lr_lat", null);
        results.put("depth", null);
        return results;
    }

}
