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
    
    <%-- Sidebar Menu (Simplified for this view) --%>
    <aside class="w-64 bg-white border-r border-gray-200 shadow-sm hidden md:block">
        <div class="h-full px-3 py-6 overflow-y-auto">
            <ul class="space-y-2 font-medium">
                <li>
                    <a href="dashboard.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
                        <span class="mr-3 text-xl">📊</span>
                        <span>Dashboard Overview</span>
                    </a>
                </li>
                <li>
                    <a href="register-appointment.jsp" class="flex items-center p-3 text-blue-700 bg-blue-50 rounded-lg group transition">
                        <span class="mr-3 text-xl">📝</span>
                        <span>Register Appointment</span>
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

    <%-- Main Content --%>
    <div class="flex-1 p-8">
        
        <div class="max-w-4xl mx-auto">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">Book New Appointment</h2>
            <p class="text-gray-600 mb-8">Schedule a new patient visit with our dental specialists.</p>

            <%-- Success/Error Messages from Servlet --%>
            <% if(request.getAttribute("message") != null) { %>
                <div class="bg-green-50 border-l-4 border-green-500 p-4 mb-6 rounded-md shadow-sm">
                    <p class="text-green-700 font-medium"><%= request.getAttribute("message") %></p>
                </div>
            <% } %>
            <% if(request.getAttribute("error") != null) { %>
                <div class="bg-red-50 border-l-4 border-red-500 p-4 mb-6 rounded-md shadow-sm">
                    <p class="text-red-700 font-medium"><%= request.getAttribute("error") %></p>
                </div>
            <% } %>

            <%-- Registration Form --%>
            <div class="bg-white rounded-xl shadow-md border border-gray-100 p-8">
                
                <form action="appointment" method="post" id="appointmentForm" class="space-y-6">
                    <input type="hidden" name="action" value="register">

                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        
                        <!-- Patient Details Section -->
                        <div class="space-y-4">
                            <h3 class="text-lg font-semibold text-gray-800 border-b pb-2">Patient Details</h3>
                            
                            <div>
                                <label for="patientName" class="block text-sm font-medium text-gray-700">Full Name <span class="text-red-500">*</span></label>
                                <input type="text" id="patientName" name="patientName" required 
                                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                            </div>

                            <div>
                                <label for="contactNumber" class="block text-sm font-medium text-gray-700">Contact Number (10 digits) <span class="text-red-500">*</span></label>
                                <input type="text" id="contactNumber" name="contactNumber" required 
                                    pattern="^0[0-9]{9}$" title="Must be exactly 10 digits starting with 0"
                                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                            </div>
                        </div>

                        <!-- Appointment Details Section -->
                        <div class="space-y-4">
                            <h3 class="text-lg font-semibold text-gray-800 border-b pb-2">Appointment Details</h3>
                            
                            <div>
                                <label for="dentistName" class="block text-sm font-medium text-gray-700">Select Dentist <span class="text-red-500">*</span></label>
                                <select id="dentistName" name="dentistName" required 
                                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                                    <option value="" disabled selected>-- Choose a Specialist --</option>
                                    <option value="Dr. Anura Silva">Dr. Anura Silva (General)</option>
                                    <option value="Dr. Nimal Fernando">Dr. Nimal Fernando (Surgeon)</option>
                                </select>
                            </div>

                            <div>
                                <label for="treatmentId" class="block text-sm font-medium text-gray-700">Select Treatment <span class="text-red-500">*</span></label>
                                <select id="treatmentId" name="treatmentId" required 
                                    class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                                    <option value="" disabled selected>-- Choose a Treatment --</option>
                                    <option value="1">Consultation Only (Rs. 0)</option>
                                    <option value="2">Teeth Cleaning (Rs. 3000)</option>
                                    <option value="5">Root Canal (Rs. 15000)</option>
                                </select>
                            </div>

                            <div class="grid grid-cols-2 gap-4">
                                <div>
                                    <label for="date" class="block text-sm font-medium text-gray-700">Date <span class="text-red-500">*</span></label>
                                    <input type="date" id="date" name="date" required 
                                        class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                                </div>
                                <div>
                                    <label for="time" class="block text-sm font-medium text-gray-700">Time <span class="text-red-500">*</span></label>
                                    <input type="time" id="time" name="time" min="08:00" max="20:00" required 
                                        class="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition">
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="flex justify-end pt-6 border-t border-gray-100">
                        <button type="reset" class="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 mr-4 transition font-medium">Clear Form</button>
                        <button type="submit" class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition font-bold btn-hover shadow-md">Confirm Appointment</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<!-- HTML5 / JS Client-Side Validation -->
<script>
    document.addEventListener("DOMContentLoaded", () => {
        // Prevent selecting past dates
        const dateInput = document.getElementById('date');
        const today = new Date().toISOString().split('T')[0];
        dateInput.setAttribute('min', today);

        // Form submission intercept for extra validation
        document.getElementById("appointmentForm").addEventListener("submit", function(event) {
            const timeInput = document.getElementById('time').value;
            
            if (timeInput) {
                const hour = parseInt(timeInput.split(":")[0]);
                if (hour < 8 || hour > 19) {
                    alert("Appointments can only be scheduled during clinic hours (08:00 to 20:00).");
                    event.preventDefault(); // Stop submission
                }
            }
        });
    });
</script>

<jsp:include page="includes/footer.jsp" />
