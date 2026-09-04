<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
    // Session Check
    if(session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<jsp:include page="includes/header.jsp" />

<div class="flex-grow flex bg-gray-50">
    <%-- Sidebar Menu --%>
    <aside class="w-64 bg-white border-r border-gray-200 shadow-sm hidden md:block">
        <div class="h-full px-3 py-6 overflow-y-auto">
            <ul class="space-y-2 font-medium">
                <li>
                    <a href="dashboard" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">📊</span>
                        <span>Dashboard Overview</span>
                    </a>
                </li>
                <li>
                    <a href="register-appointment.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">📝</span>
                        <span>Register Appointment</span>
                    </a>
                </li>
                <li>
                    <a href="billing" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">💳</span>
                        <span>Billing & Invoices</span>
                    </a>
                </li>
                <li>
                    <a href="help.jsp" class="flex items-center p-3 text-blue-700 bg-blue-50 rounded-lg group transition">
                        <span class="mr-3 text-xl">❓</span>
                        <span>Help Center</span>
                    </a>
                </li>
            </ul>
        </div>
    </aside>

    <div class="flex-1 p-8">
        <div class="max-w-4xl mx-auto">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">Help Center</h2>
            <p class="text-gray-600 mb-8">Step-by-step instructions for Sunrise Dental Clinic staff.</p>

            <div class="space-y-6">
                <!-- Registration Help -->
                <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                    <h3 class="text-xl font-bold text-blue-800 mb-4 flex items-center"><span class="mr-2">1.</span> Registering a New Appointment</h3>
                    <ol class="list-decimal list-inside space-y-2 text-gray-700 ml-4">
                        <li>Navigate to the <strong>Register Appointment</strong> page from the sidebar or top menu.</li>
                        <li>Enter the patient's full name and exactly a 10-digit contact number starting with '0'.</li>
                        <li>Select the preferred Dentist and Treatment type from the dropdowns.</li>
                        <li>Select an available date and a time between 08:00 AM and 08:00 PM.</li>
                        <li>Click <strong>Confirm Appointment</strong>. If successful, you will receive an Appointment Number (e.g., APT-20240904-XXXX).</li>
                    </ol>
                </div>

                <!-- Dashboard & Search Help -->
                <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                    <h3 class="text-xl font-bold text-blue-800 mb-4 flex items-center"><span class="mr-2">2.</span> Viewing & Searching Appointments</h3>
                    <ul class="list-disc list-inside space-y-2 text-gray-700 ml-4">
                        <li>The <strong>Dashboard Overview</strong> displays the most recent appointments booked today.</li>
                        <li>To find a specific appointment, use the <strong>Search bar</strong> on the Dashboard.</li>
                        <li>Enter the exact Appointment Number (e.g., APT-20240904-ABCD) and press Enter to view the full details.</li>
                    </ul>
                </div>

                <!-- Billing Help -->
                <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                    <h3 class="text-xl font-bold text-blue-800 mb-4 flex items-center"><span class="mr-2">3.</span> Generating a Patient Bill</h3>
                    <ol class="list-decimal list-inside space-y-2 text-gray-700 ml-4">
                        <li>Navigate to the <strong>Billing & Invoices</strong> section.</li>
                        <li>Enter the patient's <strong>Appointment Number</strong> in the search form.</li>
                        <li>The system will automatically calculate the total cost based on the standard consultation fee (Rs. 500) and the selected treatment cost.</li>
                        <li>Review the invoice and use the <strong>Print Invoice</strong> button to print a physical copy for the patient.</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
