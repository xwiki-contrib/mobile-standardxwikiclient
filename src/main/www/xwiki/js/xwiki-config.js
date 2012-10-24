/* xwiki mobile js */

sessionStorage.xwikiconfig = 0;
sessionStorage.currentwiki = "";

xwikiconfig = [
               {
               id : "local",
               name: "Local Account",
               xem : false,
               wikis : [""],
               baseurl : "http://localhost:9080/xwiki",
               resturl : "http://localhost:9080/xwiki/rest/wikis/xwiki/",
               viewurl : "http://localhost:9080/xwiki/bin/view/",
               apps : { "" : [{ name: "Bikes", space: "Bikes", classname : "BikesCode.BikesClass" }] },                            
               username : "Admin",
               password : "admin"
               },
                {
                id : "ludovic",
                name: "Ludovic's Cloud Account",
                xem : false,
                wikis : [""],
                baseurl : "http://ludovic.cloud.xwiki.com/xwiki",
                resturl : "http://ludovic.cloud.xwiki.com/xwiki/rest/wikis/wiki0/",
                viewurl : "http://ludovic.cloud.xwiki.com/xwiki/bin/view/",
                apps : { "" : [{ name: "Bikes", space: "Bikes", classname : "BikesCode.BikesClass" }] },                            
                username : "Admin",
                password : "toimoilui"
                },
               {
               id : "planete",
               name: "Planete Sankore",
               xem : false,
               wikis : [""],
               baseurl : "http://planete.sankore.org/xwiki",
               resturl : "http://planete.sankore.org/xwiki/rest/wikis/xwiki/",
               viewurl : "http://planete.sankore.org/xwiki/bin/view/",
               apps : { "" : [{ name: "Resources", space: "Resources", classname : "CurrikiCode.AssetClass" }] },                            
               username : "Admin",
               password : "sankoredemo123",
               template : "popup"
               },
               {
               id : "carrefour",
               name: "Carrefour Modèle Opérationnel",
               xem : false,
               wikis : [""],
               baseurl : "http://carrefourmarket.xwiki.com/xwiki",
               resturl : "http://carrefourmarket.xwiki.com/xwiki/rest/wikis/xwiki/",
               viewurl : "http://carrefourmarket.xwiki.com/xwiki/bin/view/",
               apps : { "" : [{ name: "Procedures", space: "MON", classname : "Code.MacroprocessusClass" }] },                            
               username : "Admin",
               password : "xwcarrouf2011"
               },
              {
               id : "xwikisas",
               name: "XWiki SAS Intranet",
               xem : true,
               wikis : ["sales", "marketing", "products", "projects", "support", "hr", "paris", "iasi", "platform", "research"],
               baseurl : "https://__wiki__.xwikisas.com/xwiki",
               resturl : "https://www.xwikisas.com/xwiki/rest/wikis/__wiki__/",
               viewurl : "https://__wiki__.xwikisas.com/xwiki/bin/view/",
               downloadurl : "https://__wiki__.xwikisas.com/xwiki/bin/download/",
               apps : { "hr" : [{ name: "Recruitment", space: "Recruitment", classname : "RecruitmentCode.CandidateClass" },
                                { name: "Holiday Requests", space: "HolidayRequest", classname : "HolidayRequestCode.HolidayRequestClass" },
                                { name: "Expense Reports", space: "ExpenseReport", classname : "ExpenseReportCode.ExpenseReportClass" },
                                { name: "Evaluations", space: "Evaluation", classname : "EvaluationCode.EvaluationMeetingClass" },
                                { name: "Employees", space: "RH", classname : "RH.EmployeeClass" },
                                { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass" }
                                ],
               "sales" : [
                          { name: "Accounts", space: "CRM", classname : "CRMClasses.CRMAccountClass" },
                          { name: "Projects", space: "CRM", classname : "CRMClasses.CRMProjectClass" },
                          { name: "Contacts", space: "CRM", classname : "CRMClasses.CRMContactClass" },
                          { name: "Invoices", space: "CRM", classname : "CRMClasses.CRMInvoiceClass" }
                          ],
               "marketing" : [
                              { name: "Presentations", space: "Presentation", classname : "PresentationsCode.PresentationsClass" },
                              { name: "Events", space: "Events", classname : "EventsCode.EventClass" },
                              { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass" }
                              ],
               "products" : [
                             { name: "Features", space: "Roadmap", classname : "IdeaCode.IdeaClass" }
                             ],
               
               "projects" : [                            
                             { name: "Projects", space: "Projects", classname : "PMCode.XProjectClass" },
                             { name: "Releases", space: "Projects", classname : "PMCode.ReleaseNoteClass" },
                             { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass" },
                             { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass2" }
                             ],
               "support" : [                            
                            { name: "Contracts", space: "Contracts", classname : "SupportCode.ContractClass" },
                            { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass" }
                            ],
               "paris" : [
                          { name: "Xambox", space: "XamBox", classname : "XamBoxCode.XamBoxDocumentClass" }
                          ],
               "iasi" : [
                         { name: "Xambox", space: "XamBox", classname : "XamBoxCode.XamBoxDocumentClass" }
                         ],
               "platform" : [
                             { name: "Machines", space: "Platform", classname : "PlatformCode.MachineClass" },
                             { name: "VMs", space: "Platform", classname : "PlatformCode.VMClass" },
                             { name: "Farms", space: "Platform", classname : "PlatformCode.FarmClass" },
                             { name: "Contracts", space: "Platform", classname : "PlatformCode.ContractClass" },
                             { name: "Contract Items", space: "Platform", classname : "PlatformCode.ContractItemClass" },
                             { name: "Meetings", space: "Meetings", classname : "MMCode.MeetingClass" }
                             ],
               "research" : []
               },                           
               username : "TestTest",
               password : "test2011"
               }             
               ]
