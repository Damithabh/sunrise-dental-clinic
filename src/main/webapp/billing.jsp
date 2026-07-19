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
                    <a href="dashboard.jsp" class="flex items-center p-3 text-gray-900 rounded-lg hover:bg-gray-100 group transition">
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
                    <a href="billing.jsp" class="flex items-center p-3 text-blue-700 bg-blue-50 rounded-lg group transition">
                        <span class="mr-3 text-xl">💳</span>
                        <span>Billing & Invoices</span>
                    </a>
                </li>
            </ul>
        </div>
    </aside>

    <%-- Main Content --%>
    <div class="flex-1 p-8">
        
        <div class="flex justify-between items-center mb-8">
            <div>
                <h2 class="text-3xl font-bold text-gray-800">Patient Invoice</h2>
                <p class="text-gray-600">Review and print billing details for completed appointments.</p>
            </div>
            <button onclick="window.print()" class="px-5 py-2 bg-gray-800 text-white rounded-lg hover:bg-gray-900 transition font-medium shadow flex items-center gap-2 btn-hover">
                <span class="text-lg">🖨️</span> Print Invoice
            </button>
        </div>

        <%-- Printable Invoice Card --%>
        <div class="max-w-3xl mx-auto bg-white rounded-none md:rounded-xl shadow-lg border border-gray-200 p-10 print:shadow-none print:border-none print:p-0">
            
            <%-- Invoice Header --%>
            <div class="flex justify-between items-start border-b-2 border-blue-600 pb-6 mb-6">
                <div>
                    <h1 class="text-3xl font-bold text-blue-800 flex items-center gap-2">
                        <span>🦷</span> Sunrise Dental
                    </h1>
                    <p class="text-gray-500 mt-1">123 Galle Road, Colombo 03</p>
                    <p class="text-gray-500">Tel: +94 11 234 5678</p>
                </div>
                <div class="text-right">
                    <h2 class="text-4xl font-black text-gray-200 uppercase tracking-widest">INVOICE</h2>
                    <p class="text-gray-800 font-bold mt-2">Invoice #: <span class="font-normal text-gray-600">INV-88392</span></p>
                    <p class="text-gray-800 font-bold">Date: <span class="font-normal text-gray-600"><%= java.time.LocalDate.now() %></span></p>
                </div>
            </div>

            <%-- Patient & Appt Info --%>
            <div class="grid grid-cols-2 gap-8 mb-10">
                <div>
                    <h3 class="text-gray-500 font-bold uppercase text-xs tracking-wider mb-2">Billed To:</h3>
                    <p class="font-bold text-gray-800 text-lg">Kamal Perera</p>
                    <p class="text-gray-600">45 Temple Road, Kandy</p>
                    <p class="text-gray-600">Tel: 0771234567</p>
                </div>
                <div class="text-right">
                    <h3 class="text-gray-500 font-bold uppercase text-xs tracking-wider mb-2">Appointment Details:</h3>
                    <p class="text-gray-800"><span class="font-bold">Appt No:</span> APT-20240715-001</p>
                    <p class="text-gray-800"><span class="font-bold">Dentist:</span> Dr. Anura Silva</p>
                </div>
            </div>

            <%-- Line Items Table --%>
            <table class="w-full mb-8">
                <thead>
                    <tr class="border-b border-gray-300 text-left text-sm font-bold text-gray-600 uppercase">
                        <th class="py-3">Description</th>
                        <th class="py-3 text-right">Amount (LKR)</th>
                    </tr>
                </thead>
                <tbody class="text-gray-800">
                    <tr class="border-b border-gray-100">
                        <td class="py-4 font-medium">Standard Consultation Fee</td>
                        <td class="py-4 text-right">500.00</td>
                    </tr>
                    <tr class="border-b border-gray-100">
                        <td class="py-4">
                            <span class="font-medium">Teeth Cleaning</span><br>
                            <span class="text-sm text-gray-500">Professional scaling and polishing (30 mins)</span>
                        </td>
                        <td class="py-4 text-right">3,000.00</td>
                    </tr>
                </tbody>
            </table>

            <%-- Totals --%>
            <div class="flex justify-end">
                <div class="w-1/2 md:w-1/3">
                    <div class="flex justify-between py-2 text-gray-600">
                        <span>Subtotal</span>
                        <span>3,500.00</span>
                    </div>
                    <div class="flex justify-between py-2 text-gray-600 border-b border-gray-300">
                        <span>Discount (0%)</span>
                        <span>0.00</span>
                    </div>
                    <div class="flex justify-between py-4 text-xl font-bold text-gray-900">
                        <span>Total Due</span>
                        <span>Rs 3,500.00</span>
                    </div>
                </div>
            </div>

            <%-- Footer / Notes --%>
            <div class="mt-12 text-center text-gray-500 text-sm print:mt-32">
                <p>Thank you for choosing Sunrise Dental Clinic.</p>
                <p>Payment is due within 14 days of invoice date.</p>
            </div>
        </div>

    </div>
</div>

<jsp:include page="includes/footer.jsp" />
