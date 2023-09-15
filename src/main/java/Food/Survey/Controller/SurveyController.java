package Food.Survey.Controller;

import Food.Survey.Service.SurveyService;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@XRayEnabled // Enable X-Ray tracing for the entire controller
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
    @GetMapping("/")
    @XRayEnabled // Enable X-Ray tracing for this method
    public String surveyPage() {
        return "survey"; // Return the HTML view name (survey.html)
    }

    @GetMapping("/getImagesFromS3")
    @ResponseBody
    @XRayEnabled // Enable X-Ray tracing for this method
    public List<String> getAllImagesFromS3Bucket() {
        // Provide your S3 bucket name
        String bucketName = "local-build-sample";
        return surveyService.getAllImagesFromS3Bucket(bucketName);
    }
    @PostMapping("/saveRating")
    @ResponseBody
    @XRayEnabled // Enable X-Ray tracing for this method
    public String saveRating(@RequestBody Map<String, Object> requestBody) {
        String imageUrl = (String) requestBody.get("imageUrl");
        int rating = (int) requestBody.get("rating");

        // Here, you can call the method in your service to save the rating
        surveyService.saveRating(imageUrl, rating);

        return "Rating saved successfully";
    }

    @GetMapping("/getAllRatings")
    @XRayEnabled // Enable X-Ray tracing for this method
    public ResponseEntity<Map<String, List<Integer>>> getAllRatings() {
        Map<String, List<Integer>> allRatings = surveyService.getAllRatings();
        return ResponseEntity.ok(allRatings);
    }
}
