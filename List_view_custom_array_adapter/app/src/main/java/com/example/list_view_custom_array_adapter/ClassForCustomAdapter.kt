package com.example.list_view_custom_array_adapter

class ClassForCustomAdapter() {
    private var name: String = ""
    private var skill: String = ""
    constructor(name : String, skill : String) : this() {
        this.name = name
        this.skill = skill
    }
    fun getName () : String {
        return name
    }
    fun getSkill () : String {
        return skill
    }
    fun listOfNamesWithSkills () : ArrayList<ClassForCustomAdapter>{
        var array_list = ArrayList<ClassForCustomAdapter>()
        array_list.add(ClassForCustomAdapter("Abhi", "Developer"))
        array_list.add(ClassForCustomAdapter("Abhishek", "C++"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "DSA"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "Python"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "ML"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "HTML"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "CSS"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "XML"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "JAVA"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "SQL"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "C"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "Math"))
        array_list.add(ClassForCustomAdapter("Abhishek Bansal", "Javascript"))

        array_list.add(ClassForCustomAdapter("Aman", "Developer"))
        array_list.add(ClassForCustomAdapter("Aman", "C++"))
        array_list.add(ClassForCustomAdapter("Aman", "DSA"))
        array_list.add(ClassForCustomAdapter("Aman", "Python"))
        array_list.add(ClassForCustomAdapter("Aman", "ML"))
        array_list.add(ClassForCustomAdapter("Aman", "HTML"))
        array_list.add(ClassForCustomAdapter("Aman", "CSS"))
        array_list.add(ClassForCustomAdapter("Aman", "XML"))
        array_list.add(ClassForCustomAdapter("Aman", "JAVA"))
        array_list.add(ClassForCustomAdapter("Aman", "SQL"))
        array_list.add(ClassForCustomAdapter("Aman", "C"))
        array_list.add(ClassForCustomAdapter("Aman", "Math"))

        return array_list
    }
}