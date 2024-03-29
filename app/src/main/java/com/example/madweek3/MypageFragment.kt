package com.example.madweek3

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.madweek3.databinding.FragmentMypageBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class MypageFragment : Fragment() {
    private var userId: String = ""
    private var userNickname: String = ""
    private var userEmail: String = ""
    private var userPW: String = ""
    private var userLevel: String = ""
    private var userScore: Int = 0
    private lateinit var questionBtn: Button
    private lateinit var modifyBtn: Button
    private lateinit var sechanImg: ImageView
    private lateinit var levelView: TextView
    private lateinit var nicknameView: TextView
    private lateinit var emailView: TextView
    private lateinit var pwView: TextView
    private lateinit var pwLayout: LinearLayout
    private lateinit var sechan_imgs_perLevel: JSONObject

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sechan_imgs_perLevel = JSONObject("{\"잼민이 세찬\": \"level1\", \"깡깡이 세찬\": \"level2\", \"군기바짝 세찬\": \"level3\", \"덱스? 세찬\": \"level4\", \"출세한 세찬\": \"level5\", \"슬픈데 기쁜 광수\": \"level6\"}")
        println(sechan_imgs_perLevel)



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        questionBtn = view.findViewById(R.id.questionBtn)
        modifyBtn = view.findViewById(R.id.modifyBtn)
        sechanImg = view.findViewById(R.id.imageSechan)
        levelView = view.findViewById(R.id.mypageLevel)
        nicknameView = view.findViewById(R.id.mypageNickname)
        emailView = view.findViewById(R.id.mypageEmail)

        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "")?:""

        lifecycleScope.launch {
            val getUserInfoDeferred = async { getInfo() }
            getUserInfoDeferred.await()
            setUpUI()
        }

        return view
    }

    suspend fun getInfo() {
        try {
            val response = RetrofitClient.instance.getUserInfo(userId)
            if (response.isSuccessful && response.body() != null) {
                userNickname = response.body()!!.nickname
                userEmail = response.body()!!.email
                userPW = response.body()!!.password
                userLevel = response.body()!!.level
                userScore = response.body()!!.score
            } else {
                Log.d("response failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Get Userinfo Error", "error: ${e.localizedMessage}}")
        }
    }

    private fun setUpUI() {
        levelView.text = userLevel
        nicknameView.text = userNickname
        emailView.text = userEmail
        val resource_name = sechan_imgs_perLevel.getString(userLevel)
        val packageName = "com.example.madweek3"
        val resourceId = resources.getIdentifier(resource_name, "drawable", packageName)

        if (resourceId != 0) {
            sechanImg.setImageResource(resourceId)
        }

        //물음표 버튼 눌렀을 때
        questionBtn.setOnClickListener {
            val levelDescription = LevelExplainFragment(requireContext())
            levelDescription.show()
        }

        //수정 버튼 눌렀을 때
        modifyBtn.setOnClickListener {
            //수정 프래그먼트로 이동
            val modify = ModifyFragment()
            val bundle = Bundle()
            bundle.putString("userID", userId)
            bundle.putString("email", userEmail)
            bundle.putString("nickname", userNickname)
            bundle.putString("password", userPW)
            bundle.putString("level", userLevel)
            bundle.putInt("score", userScore)
            modify.arguments = bundle

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, modify)
            transaction.commit()
        }

    }
}