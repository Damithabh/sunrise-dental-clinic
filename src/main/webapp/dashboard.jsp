<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% 
    // Session Check: Prevent access if not logged in
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
                    <a href="dashboard.jsp" class="flex items-center p-3 text-blue-700 bg-blue-50 rounded-lg group transition">
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
                    <a href="#" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">🔍</span>
                        <span>Search Patients</span>
                    </a>
                </li>
                <li>
                    <a href="billing.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">💳</span>
                        <span>Billing & Invoices</span>
                    </a>
                </li>
            </ul>
        </div>
    </aside>

    <%-- Main Dashboard Content --%>
    <div class="flex-1 p-8">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Clinic Overview</h2>
        
        <%-- Analytics Cards Grid --%>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            
            <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 transform transition-all duration-300 hover:-translate-y-1 hover:shadow-md">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-sm font-medium text-gray-500 uppercase tracking-wider">Today's Appointments</h3>
                    <span class="p-2 bg-blue-100 text-blue-800 rounded-lg text-xl">📅</span>
                </div>
                <div class="text-3xl font-bold text-gray-900">12</div>
                <p class="text-sm text-green-600 mt-2">↑ 3 from yesterday</p>
            </div>

            <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 transform transition-all duration-300 hover:-translate-y-1 hover:shadow-md">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-sm font-medium text-gray-500 uppercase tracking-wider">Pending Bills</h3>
                    <span class="p-2 bg-yellow-100 text-yellow-800 rounded-lg text-xl">⚠️</span>
                </div>
                <div class="text-3xl font-bold text-gray-900">4</div>
                <p class="text-sm text-gray-500 mt-2">Requires follow-up</p>
            </div>

            <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 transform transition-all duration-300 hover:-translate-y-1 hover:shadow-md">
                <div class="flex items-center justify-between mb-4">
                    <h3 class="text-sm font-medium text-gray-500 uppercase tracking-wider">Total Revenue</h3>
                    <span class="p-2 bg-green-100 text-green-800 rounded-lg text-xl">💰</span>
                </div>
                <div class="text-3xl font-bold text-gray-900">Rs 45,500</div>
                <p class="text-sm text-green-600 mt-2">Generated today</p>
            </div>

        </div>

        <%-- Recent Activity Table --%>
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="px-6 py-4 border-b border-gray-200">
                <h3 class="text-lg font-bold text-gray-800">Recent Appointments</h3>
            </div>
            <div class="overflow-x-auto">
                <table class="w-full text-sm text-left text-gray-500">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                        <tr>
                            <th scope="col" class="px-6 py-3">Appt Number</th>
                            <th scope="col" class="px-6 py-3">Patient Name</th>
                            <th scope="col" class="px-6 py-3">Treatment</th>
                            <th scope="col" class="px-6 py-3">Time</th>
                            <th scope="col" class="px-6 py-3">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="bg-white border-b hover:bg-gray-50">
                            <td class="px-6 py-4 font-medium text-blue-600">APT-20240719-001</td>
                            <td class="px-6 py-4">Kamal Perera</td>
                            <td class="px-6 py-4">Teeth Cleaning</td>
                            <td class="px-6 py-4">09:00 AM</td>
                            <td class="px-6 py-4"><span class="bg-green-100 text-green-800 text-xs font-semibold px-2.5 py-0.5 rounded">Completed</span></td>
                        </tr>
                        <tr class="bg-white border-b hover:bg-gray-50">
                            <td class="px-6 py-4 font-medium text-blue-600">APT-20240719-002</td>
                            <td class="px-6 py-4">Saman De Silva</td>
                            <td class="px-6 py-4">Root Canal</td>
                            <td class="px-6 py-4">11:30 AM</td>
                            <td class="px-6 py-4"><span class="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">In Progress</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
