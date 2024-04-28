package com.self.flipcart.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DistrictList {
    static String[] andhra_pradesh = {"Anantapur", "Chittoor", "East Godavari", "Guntur", "Krishna", "Kurnool", "Nellore", "Prakasam", "Srikakulam", "Visakhapatnam", "Vizianagaram", "West Godavari", "YSR Kadapa"};
    static String[] arunachalPradesh = {"Anjaw", "Changlang", "East Kameng", "East Siang", "Kamle", "Kra Daadi", "Kurung Kumey", "Lohit", "Lower Dibang Valley", "Lower Siang", "Papum Pare", "Siang", "Tawang", "Tirap", "Upper Dibang Valley", "Upper Siang", "West Kameng", "West Siang"};
    static String[] assam = {"Barpeta", "Biswanath", "Bongaigaon", "Cachar", "Charaideo", "Chirang", "Darrang", "Dhemaji", "Dhubri", "Dibrugarh", "Goalpara", "Hailakandi", "Hojai", "Jorhat", "Kamrup", "Kamrup Metropolitan", "Karbi Anglong", "Karimganj", "Lakhimpur", "Majuli", "Morigaon", "Nagaon", "Nalbaria", "Sivasagar", "Sonitpur", "South Salmara", "Tinsukia", "Udalguri", "West Karbi Anglong"};
    static String[] bihar = {"Araria", "Arwal", "Aurangabad", "Banka", "Begusarai", "Bhabua", "Bhojpur", "Buksa", "Darbhanga", "East Champaran", "Gaya", "Gopalganj", "Jamui", "Jehanabad", "Kaimur (Bhabua)", "Katihar", "Khagaria", "Kishanganj", "Lakhisarai", "Madhubani", "Munger", "Muzaffarpur", "Nalanda", "Nawada", "Patna", "Purnia", "Rohtas", "Saharsa", "Samastipur", "Saran", "Sheohar", "Sitamarhi", "Siwan", "Supaul", "Vaishali", "West Champaran"};
    static String[] chandigarh = {"Chandigarh"};
    static String[] chhattisgarh = {"Balod", "Baloda Bazar", "Balrampur", "Bastar", "Bijapur", "Bilaspur", "Dantewada", "Dhamtari", "Durg", "Gariaband", "Janjgir-Champa", "Kabeerdham", "Kanker", "Kondagaon", "Korba", "Mahasamund", "Mungeli", "Narayanpur", "Raigarh", "Rajnandgaon", "Sukma", "Surajpur"};
    static String[] delhi = {"Central Delhi", "East Delhi", "New Delhi", "North Delhi", "North East Delhi", "North West Delhi", "South Delhi", "South East Delhi", "South West Delhi", "West Delhi"};
    static String[] goa = {"North Goa", "South Goa"};
    static String[] gujarat = {"Ahmedabad", "Amreli", "Anand", "Ankleshwar", "Banaskantha", "Bharuch", "Bhavnagar", "Botad", "Chhota Udaipur", "Dahod", "Dang", "Devbhumi Dwarka", "Gandhinagar", "Gir Somnath", "Jamnagar", "Junagadh", "Kachchh", "Kheda", "Mahisagar", "Mahesana", "Mehsana", "Morbi", "Narmada", "Navsari", "Panchmahal", "Patan", "Porbandar", "Rajkot", "Sabarkantha", "Surat", "Surendranagar", "Tapi", "Vadodara", "Valsad"};
    static String[] haryana = {"Ambala", "Bhiwani", "Charkhi Dadri", "Faridabad", "Fatehabad", "Gurugram", "Hisar", "Jhajjar", "Jind", "Kaithal", "Karnal", "Kurukshetra", "Mahendragarh", "Nuh", "Palwal", "Panchkula", "Rewari", "Rohtak", "Sirsa", "Sonipat", "Yamunanagar"};
    static String[] himachalPradesh = {"Bilaspur", "Chamba", "Hamirpur", "Kangra", "Kinnaur", "Kullu", "Lahaul and Spiti", "Mandi", "Shimla", "Sirmaur", "Solan", "Una"};
    static String[] jammu = {"Doda", "Kathua", "Kishtwar", "Rajouri", "Reasi", "Samba", "Udhampur"};
    static String[] kashmir = {"Anantnag", "Bandipora", "Baramulla", "Budgam", "Kulgam", "Kupwara", "Pattan", "Pulwama", "Shopian", "Srinagar"};
    static String[] ladakh = {"Kargil", "Leh"};
    static String[] jharkhand = {"Bokaro", "Chatra", "Deoghar", "Dhanbad", "Dumka", "East Singhbhum", "Garhwa", "Giridih", "Godda", "Gumla", "Hazaribagh", "Jamtara", "Khunti", "Koderma", "Latehar", "Lohardaga", "Pakur", "Palamu", "Ramgarh", "Ranchi", "Sahebganj", "Seraikela-Kharsawan", "Simdega", "West Singhbhum"};
    static String[] karnataka = {"Bagalkot", "Ballari", "Bengaluru Rural", "Bengaluru Urban", "Bidar", "Chamarajanagara", "Chikkamagaluru", "Chitradurga", "Dakshina Kannada", "Davangere", "Dharwad", "Gadag", "Haveri", "Kalaburagi", "Kodagu", "Kolar", "Koppal", "Mandya", "Mysuru", "Raichur", "Ramanagara", "Shivamogga", "Tumakuru", "Udupi", "Vijayapura", "Yadgir"};
    static String[] kerala = {"Alappuzha", "Ernakulam", "Idukki", "Kannur", "Kasaragod", "Kottayam", "Kozhikode", "Malappuram", "Palakkad", "Pathanamthitta", "Thiruvananthapuram", "Thrissur", "Wayanad"};
    static String[] lakshadweep = {"Agatti", "Amini", "Andrott", "Bangaram", "Chetlat", "Kadmat", "Kalpeni", "Kavaratti", "Minicoy", "Suheli"};
    static String[] madhyaPradesh = {"Agar Malwa", "Alirajpur", "Anuppur", "Ashok Nagar", "Balaghat", "Barwani", "Betul", "Bhind", "Bhopal", "Burhanpur", "Chhatarpur", "Chhindwara", "Damoh", "Datia", "Dewas", "Dhar", "Dindori", "Gwalior", "Harda", "Hoshangabad", "Indore", "Jabalpur", "Jagdalpur", "Jhabua", "Katni", "Khandwa", "Khargone", "Mandla", "Mandsaur", "Morena", "Narmadapuram", "Neemuch", "Niwari", "Panna", "Raisen", "Rajgarh", "Rewa", "Sagar", "Satna", "Sehore", "Seoni", "Shahdol", "Shajapur", "Sheopur", "Shivpuri", "Singrauli", "Sonipat", "Stree Shakti Nagar", "Ujjain", "Umaria", "Vidisha"};
    static String[] maharashtra = {"Ahmednagar", "Akola", "Amravati", "Aurangabad", "Beed", "Bhandara", "Buldhana", "Chandrapur", "Dhule", "Gadchiroli", "Gondia", "Hingoli", "Jalgaon", "Jalna", "Kolhapur", "Latur", "Mumbai City", "Mumbai Suburban", "Nagpur", "Nanded", "Nandurbar", "Nashik", "Osmanabad", "Palghar", "Parbhani", "Pune", "Raigad", "Ratnagiri", "Sangli", "Satara", "Sindhudurg", "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"};
    static String[] manipur = {"Bishnupur", "Chandel", "Churachandpur", "Imphal East", "Imphal West", "Kamjong", "Kakching", "Senapati", "Tamenglong", "Thoubal", "Ukhrul"};
    static String[] meghalay = {"East Jaintia Hills", "East Khasi Hills", "North Garo Hills", "Ri Bhoi", "South Garo Hills", "South West Garo Hills", "West Garo Hills", "West Khasi Hills"};
    static String[] mizoram = {"Aizawl", "Champhai", "Kolasib", "Lawngtlai", "Lunglei", "Mamit", "Saiha", "Serchhip"};
    static String[] nagaland = {"Dimapur", "Kiphire", "Kohima", "Longleng", "Mokokchung", "Mon", "Peren", "Phek", "Tuensang", "Wokha", "Zunheboto"};
    static String[] odisha = {"Angul", "Balangir", "Balasore", "Baragarh", "Baudh", "Bhadrak", "Bolangir", "Boudh", "Cuttack", "Debagarh", "Dhenkanal", "Gajapati", "Ganjam", "Jharsuguda", "Kalahandi", "Kandhamal", "Kendrapara", "Kendujhar", "Khordha", "Koraput", "Mayurbhanj", "Nabarangpur", "Nuapada", "Puri", "Rayagada", "Sambalpur", "Sonepur", "Sundargarh", "Yajpur"};
    static String[] puducherry = {"Karaikal", "Mahe", "Puducherry", "Yanam"};
    static String[] punjab = {"Amritsar", "Barnala", "Bathinda", "Chandigarh", "Fazilka", "Firozpur", "Gurdaspur", "Hoshiarpur", "Jalandhar", "Kapurthala", "Ludhiana", "Mansa", "Mohali", "Muktsar", "Pathankot", "Patiala", "Rupnagar", "Sangrur", "Shaheed Bhagat Singh Nagar", "Tarn Taran"};
    static String[] rajasthan = {"Ajmer", "Alwar", "Banswara", "Baran", "Barmer", "Bharatpur", "Bhilwara", "Bikaner", "Bundi", "Chittorgarh", "Churu", "Dausa", "Dholpur", "Dungarpur", "Hanumangarh", "Jaipur", "Jaisalmer", "Jalore", "Jodhpur", "Jhunjhunu", "Karauli", "Kota", "Nagaur", "Pali", "Pratapgarh", "Rajsamand", "Sawai Madhopur", "Sikar", "Sirohi", "Tonk", "Udaipur"};
    static String[] sikkim = {"East Sikkim", "North Sikkim", "South Sikkim", "West Sikkim"};
    static String[] tamilNadu = {"Ariyalur", "Chennai", "Chengalpattu", "Coimbatore", "Cuddalore", "Dharmapuri", "Dindigul", "Erode", "Kanchipuram", "Kanyakumari", "Karur", "Krishnagiri", "Madurai", "Nagapattinam", "Namakkal", "Nilgiris", "Perambalur", "Pudukkottai", "Ramanathapuram", "Salem", "Sivaganga", "Thanjavur", "The Nilgiris", "Theni", "Thoothukudi", "Tiruchirappalli", "Tirunelveli", "Tirupattur", "Tirupur", "Tiruvannamalai", "Vellore", "Viluppuram"};
    static String[] telangana = {"Adilabad", "Bhadradri Kothagudem", "Hyderabad", "Jagtial", "Jangaon", "Kamareddy", "Karimnagar", "Khammam", "Mahabubnagar", "Mancherial", "Medchal", "Nagarkurnool", "Nalgonda", "Narayanpet", "Nizamabad", "Peddapalli", "Rajanna Sircilla", "Rangareddy", "Sangareddy", "Siddipet", "Suryapet", "Telangana", "Wanaparthy", "Yadadri Bhuvanagiri"};
    static String[] tripura = {"Dhalai", "Gomati", "North Tripura", "South Tripura", "Sipahijala", "West Tripura"};
    static String[] uttarPradesh = {"Agra", "Aligarh", "Allahabad", "Ambedkar Nagar", "Amethi", "Amroha", "Auraiya", "Azamgarh", "Bagpat", "Bahraich", "Ballia", "Balrampur", "Banda", "Barabanki", "Bareilly", "Basti", "Bhadohi", "Bijnor", "Budaun", "Bulandshahr", "Chandauli", "Chhatrapati Shahuji Maharaj Nagar", "Chitrakoot", "Deoria", "Etah", "Etawah", "Faizabad", "Farrukhabad", "Fatehpur", "Firozabad", "Gautam Buddha Nagar", "Ghaziabad", "Ghazipur", "Gonda", "Gorakhpur", "Hamirpur", "Hardoi", "Hathras", "Jalaun", "Jaunpur", "Jhansi", "Kannauj", "Kanpur Dehat", "Kanpur Nagar", "Kanshiram Nagar", "Kaushambi", "Kushinagar", "Lalitpur", "Lucknow", "Maharajganj", "Mahoba", "Mainpuri", "Mathura", "Mau", "Meerut", "Mirzapur", "Moradabad", "Muzaffarnagar", "Noida", "Palwal", "Partapgarh", "Prayagraj", "Rae Bareli", "Rampur", "Saharanpur", "Sambhal", "Sant Kabir Nagar", "Shahjahanpur", "Shamli", "Shravasti", "Siddharthnagar", "Sitapur", "Sonbhadra", "Sultanpur", "Unnao", "Varanasi"};
    static String[] uttarakhand = {"Almora", "Bageshwar", "Chamoli", "Champawat", "Dehradun", "Haridwar", "Nainital", "Pauri Garhwal", "Pithoragarh", "Rudraprayag", "Tehri Garhwal", "Udham Singh Nagar"};
    static String[] westBengal = {"Bankura", "Birbhum", "Cooch Behar", "Dakshin Dinajpur", "Darjeeling", "East Burdwan", "Howrah", "Hooghly", "Jalpaiguri", "Jangalpur", "Kalimpong", "Kolkata", "Malda", "Murshidabad", "Nadia", "North 24 Parganas", "Paschim Burdwan", "Purba Medinipur", "Purulia", "South 24 Parganas", "Uttar Dinajpur", "West Medinipur"};

    private static final Map<String, String[]> districts = Collections.unmodifiableMap(new HashMap<>() {
        {
            put("andhra_pradesh", andhra_pradesh);
            put("arunachal_pradesh", arunachalPradesh);
            put("assam", assam);
            put("bihar", bihar);
            put("chandigarh", chandigarh);
            put("chhattisgarh", chhattisgarh);
            put("delhi", delhi);
            put("goa", goa);
            put("gujarat", gujarat);
            put("haryana", haryana);
            put("himachal_pradesh", himachalPradesh);
            put("jammu", jammu);
            put("kashmir", kashmir);
            put("ladakh", ladakh);
            put("jharkhand", jharkhand);
            put("karnataka", karnataka);
            put("kerala", kerala);
            put("lakshadweep", lakshadweep);
            put("madhya_pradesh", madhyaPradesh);
            put("maharashtra", maharashtra);
            put("manipur", manipur);
            put("meghalay", meghalay);
            put("mizoram", mizoram);
            put("nagaland", nagaland);
            put("odisha", odisha);
            put("puducherry", puducherry);
            put("punjab", punjab);
            put("rajasthan", rajasthan);
            put("sikkim", sikkim);
            put("tamilNadu", tamilNadu);
            put("telangana", telangana);
            put("tripura", tripura);
            put("uttar_pradesh", uttarPradesh);
            put("uttarakhand", uttarakhand);
            put("west_bengal", westBengal);
        }
    });

    public static String[] of(String stateName){
        return districts.get(stateName);
    }
}
